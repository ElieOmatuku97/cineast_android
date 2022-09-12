package elieomatuku.cineast_android.details.movie

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.contents.ContentsActivity
import elieomatuku.cineast_android.databinding.FragmentContentDetailsBinding
import elieomatuku.cineast_android.details.BareOverviewWidget
import elieomatuku.cineast_android.details.movie.movie_team.MovieTeamWidget
import elieomatuku.cineast_android.details.movie.overview.MovieOverviewWidget
import elieomatuku.cineast_android.domain.model.*
import elieomatuku.cineast_android.utils.*
import elieomatuku.cineast_android.widgets.MoviesWidget
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class MovieFragment : BaseFragment() {
    companion object {
        const val MOVIE_OVERVIEW = "overview"
        const val MOVIE_CREW = "crew"
        const val SIMILAR_MOVIES = "similar_movies"
    }

    lateinit var movie: Movie
    private var isInWatchList: Boolean = false
    private var isInFavoriteList: Boolean = false

    private var _binding: FragmentContentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: MovieFragmentArgs by navArgs()
    private lateinit var menuHost: MenuHost

    private val watchListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create()
    }

    private val favoriteListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create()
    }

    private val viewModel: MovieViewModel by sharedViewModel()

    private val onProfileClickedPicturePublisher: PublishSubject<Int> by lazy {
        PublishSubject.create()
    }

    private val onProfileClickedPictureObservable: Observable<Int>
        get() = onProfileClickedPicturePublisher.hide()

    private val onProfileLinkClickedPublisher: PublishSubject<String> by lazy {
        PublishSubject.create()
    }

    private val onProfileLinkClickedObservable: Observable<String>
        get() = onProfileLinkClickedPublisher.hide()

    private val segmentedButtonsPublisher: PublishSubject<Pair<String, MovieSummary>> by lazy {
        PublishSubject.create()
    }

    private val segmentedButtonsObservable: Observable<Pair<String, MovieSummary>>
        get() = segmentedButtonsPublisher.hide()

    private val adapter: MovieSummaryAdapter by lazy {
        MovieSummaryAdapter(
            onProfileClickedPicturePublisher,
            segmentedButtonsPublisher,
            onProfileLinkClickedPublisher
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListView()

        menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.item_menu, menu)

                menu.findItem(R.id.action_share).apply {
                    isVisible = ContentUtils.supportsShare(movie.id)
                    UiUtils.tintMenuItem(this, requireContext(), R.color.color_orange_app)
                }

                menu.findItem(R.id.action_watchlist).apply {
                    isChecked = isInWatchList
                    updateWatchListIcon(this)
                    isVisible = viewModel.isLoggedIn()
                }

                menu.findItem(R.id.action_favorites).apply {
                    isChecked = isInFavoriteList
                    updateFavoriteListIcon(this)
                    isVisible = viewModel.isLoggedIn()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_share -> {
                        onShareMenuClicked()
                        true
                    }

                    R.id.action_watchlist -> {
                        onWatchListMenuClicked(menuItem)
                        true
                    }

                    R.id.action_favorites -> {
                        onFavoriteListMenuClicked(menuItem)
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.composeviewContainer.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

        movie = args.movie
        viewModel.getMovieDetails(movie, args.screenName)
        viewModel.viewState.observe(viewLifecycleOwner) {
            if (it.isLoading) {
                showLoading(binding.root)
            } else {
                hideLoading(binding.root)
            }

//            binding.toolbar.title = it.screenName

            val movieSummary = it.movieSummary
            if (movieSummary != null) {
                showMovie(movieSummary)
            }

            updateWatchList(it.isInWatchList)
            updateFavorite(it.isInFavorites)

            it.viewError?.consume()?.apply {
                updateErrorView(this.message)
            }

            it.isLoggedIn.consume { isLoggedIn ->
                if (isLoggedIn) {
                    viewModel.getFavorites()
                    viewModel.getWatchLists()
                }
            }
        }
    }

    override fun onResume() {
        rxSubs.add(
            watchListCheckPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { event: Boolean ->
                        updateWatchList(event)
                    },
                    { t: Throwable ->
                        Timber.e("userListCheckPublisher failed: $t")
                    }
                )
        )

        rxSubs.add(
            favoriteListCheckPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { event: Boolean ->
                        updateFavorite(event)
                    },
                    { t: Throwable ->
                        Timber.e("favoriteListCheckPublisher failed: $t")
                    }
                )
        )

        rxSubs.add(
            onProfileClickedPictureObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    navigateToGallery()
                }
        )

        rxSubs.add(
            segmentedButtonsObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { displayAndMovieSummary ->
                    gotoTab(displayAndMovieSummary)
                }
        )

        rxSubs.add(
            onProfileLinkClickedObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { url ->
                    goToWebsite(url)
                }
        )

        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateWatchList(event: Boolean) {
        isInWatchList = event
        menuHost.invalidateMenu()
    }

    private fun updateFavorite(event: Boolean) {
        isInFavoriteList = event
        menuHost.invalidateMenu()
    }

    private fun onShareMenuClicked() {
        val shareIntent: Intent? = UiUtils.getShareIntent(movie.title, movie.id)
        // Make sure there is an activity that supports the intent
        shareIntent?.apply {
            if (resolveActivity(requireContext().packageManager) != null) {
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
            }
        }
    }

    private fun onWatchListMenuClicked(item: MenuItem) {
        item.isChecked = !item.isChecked
        val checked = item.isChecked
        updateWatchListIcon(item)

        if (checked) {
            viewModel.addMovieToWatchList()
        } else {
            viewModel.removeMovieFromWatchList()
        }
    }

    private fun updateWatchListIcon(item: MenuItem) {
        val colorRes = R.color.color_orange_app
        if (item.isChecked) {
            item.icon =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_nav_watch_list_selected,
                    requireContext().theme
                )
        } else {
            item.icon = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_nav_watch_list_unselected,
                requireContext().theme
            )
            UiUtils.tintMenuItem(item, requireContext(), colorRes)
        }
    }

    private fun onFavoriteListMenuClicked(item: MenuItem) {
        item.isChecked = !item.isChecked
        val checked = item.isChecked
        updateFavoriteListIcon(item)

        if (checked) {
            viewModel.addMovieToFavoriteList()
        } else {
            viewModel.removeMovieFromFavoriteList()
        }
    }

    private fun updateFavoriteListIcon(item: MenuItem) {
        val colorRes = R.color.color_orange_app
        if (item.isChecked) {
            item.icon =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_star_black_selected,
                    requireContext().theme
                )
        } else {
            item.icon = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_star_border_black_unselected,
                requireContext().theme
            )
            UiUtils.tintMenuItem(item, requireContext(), colorRes)
        }
    }

    private fun setUpListView() {
        binding.listViewContainer.adapter = adapter
        binding.listViewContainer.layoutManager = LinearLayoutManager(requireContext())

        val itemDecorationDrawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.item_decoration,
            activity?.theme
        )
        val dividerItemDecoration = DividerItemDecorator(itemDecorationDrawable)

        binding.listViewContainer.addItemDecoration(dividerItemDecoration)
    }

    private fun showMovie(movieSummary: MovieSummary) {
        adapter.movieSummary = movieSummary
        adapter.notifyDataSetChanged()

        binding.composeviewContainer.setContent {
            AppCompatTheme {
                MovieOverviewWidget(
                    overviewTitle = getString(R.string.plot_summary),
                    movieSummary = movieSummary,
                    onTrailerClick = { trailer ->
                        trailer.key?.let {
                            showTrailer(it)
                        }
                    }) { title, overview ->
                    BareOverviewWidget(title = title, overview = overview)
                }
            }
        }
    }

    private fun navigateToGallery() {
        val directions =
            MovieFragmentDirections.navigateToGallery(
                viewModel.posters()
                    .map { it.filePath }
                    .toTypedArray()
            )
        findNavController().navigate(directions)
    }

    private fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
        binding.listViewContainer.visibility = View.VISIBLE
    }

    private fun gotoTab(displayAndMovieSummary: Pair<String, MovieSummary>) {
        binding.composeviewContainer.setContent {
            AppCompatTheme {
                when (displayAndMovieSummary.first) {
                    SIMILAR_MOVIES -> {
                        val movieSummary: MovieSummary = displayAndMovieSummary.second
                        val similarMovies: List<Movie> = movieSummary.similarMovies ?: listOf()
                        MoviesWidget(
                            viewModelFactory = viewModelFactory,
                            movies = similarMovies,
                            sectionTitle = movie.title ?: getString(R.string.movies),
                            onItemClick = { content, _ ->
                                gotoMovie(content)
                            },
                            onSeeAllClick = { movies ->
                                ContentsActivity.startActivity(
                                    requireContext(),
                                    movies,
                                    R.string.movies
                                )
                            }
                        )
                    }
                    MOVIE_CREW -> {
                        val cast = displayAndMovieSummary.second.cast
                        val crew = displayAndMovieSummary.second.crew
                        if (cast != null && crew != null) {
                            MovieTeamWidget(
                                cast = cast,
                                crew = crew,
                                onItemClick = ::gotoPerson
                            ) {
                                ContentsActivity.startActivity(
                                    requireContext(),
                                    it,
                                    R.string.people
                                )
                            }
                        }
                    }
                    MOVIE_OVERVIEW -> {
                        MovieOverviewWidget(
                            overviewTitle = getString(R.string.plot_summary),
                            movieSummary = displayAndMovieSummary.second,
                            onTrailerClick = { trailer ->
                                trailer.key?.let {
                                    showTrailer(it)
                                }
                            }) { title, overview ->
                            BareOverviewWidget(title = title, overview = overview)
                        }
                    }
                }
            }
        }
    }

    private fun showTrailer(trailerKey: String) {
        val directions = MovieFragmentDirections.navigateToVideo(trailerKey)
        findNavController().navigate(directions)
    }

    private fun gotoPerson(person: Content) {
        if (person is Person) {
            val directions = MovieFragmentDirections.navigateToPersonDetail(
                movie.title ?: String(),
                person
            )
            findNavController().navigate(directions)
        }
    }

    private fun gotoMovie(content: Content) {
        if (content is Movie) {
            val directions = MovieFragmentDirections.navigateToMovieDetail(
                movie.title ?: String(),
                content
            )
            findNavController().navigate(directions)
        }
    }

    private fun goToWebsite(url: String) {
        val directions = MovieFragmentDirections.navigateToWebsite(url)
        findNavController().navigate(directions)
    }
}
