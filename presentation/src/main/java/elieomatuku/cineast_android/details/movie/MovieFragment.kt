package elieomatuku.cineast_android.details.movie

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.databinding.FragmentContentDetailsBinding
import elieomatuku.cineast_android.details.BareOverviewWidget
import elieomatuku.cineast_android.details.movie.movie_team.MOVIE_TEAM_KEY
import elieomatuku.cineast_android.details.movie.movie_team.MovieTeamWidget
import elieomatuku.cineast_android.details.movie.movie_team.PEOPLE_KEY
import elieomatuku.cineast_android.details.movie.overview.MovieOverviewWidget
import elieomatuku.cineast_android.details.person.PersonFragment
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.utils.*
import elieomatuku.cineast_android.widgets.MOVIE_GENRES_KEY
import elieomatuku.cineast_android.widgets.MOVIE_KEY
import elieomatuku.cineast_android.widgets.MoviesWidget
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.io.Serializable

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

    private val watchListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
    }

    private val favoriteListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
    }

    private val viewModel: MovieViewModel by sharedViewModel()

    private val onProfileClickedPicturePublisher: PublishSubject<Int> by lazy {
        PublishSubject.create<Int>()
    }

    private val onProfileClickedPictureObservable: Observable<Int>
        get() = onProfileClickedPicturePublisher.hide()

    private val segmentedButtonsPublisher: PublishSubject<Pair<String, MovieSummary>> by lazy {
        PublishSubject.create<Pair<String, MovieSummary>>()
    }

    private val segmentedButtonsObservable: Observable<Pair<String, MovieSummary>>
        get() = segmentedButtonsPublisher.hide()

    private val adapter: MovieSummaryAdapter by lazy {
        MovieSummaryAdapter(onProfileClickedPicturePublisher, segmentedButtonsPublisher)
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

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.inflateMenu(R.menu.item_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_share -> {
                    onShareMenuClicked()
                    true
                }

                R.id.action_watchlist -> {
                    onWatchListMenuClicked(it)
                    true
                }

                R.id.action_favorites -> {
                    onFavoriteListMenuClicked(it)
                    true
                }

                else -> false
            }
        }
        binding.composeviewContainer.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

        movie = args.movie
        viewModel.getMovieDetails(movie, args.screenName)
        viewModel.viewState.observe(viewLifecycleOwner) {

            if (it.isLoading) {
                showLoading(binding.root)
            } else {
                hideLoading(binding.root)
            }

            binding.toolbar.title = it.screenName

            val movieSummary = it.movieSummary
            if (movieSummary != null) {
                showMovie(movieSummary)
            }

            updateActionShare()
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

        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateActionShare() {
        binding.toolbar.menu?.findItem(R.id.action_share)?.apply {
            isVisible = ContentUtils.supportsShare(movie.id)
            UiUtils.tintMenuItem(this, requireContext(), R.color.color_orange_app)
        }
    }

    private fun updateWatchList(event: Boolean) {
        isInWatchList = event
        binding.toolbar.menu?.findItem(R.id.action_watchlist)?.apply {
            isChecked = isInWatchList
            updateWatchListIcon(this)
            isVisible = viewModel.isLoggedIn()
        }
    }

    private fun updateFavorite(event: Boolean) {
        isInFavoriteList = event
        binding.toolbar.menu?.findItem(R.id.action_favorites)?.apply {
            isChecked = isInFavoriteList
            updateFavoriteListIcon(this)
            isVisible = viewModel.isLoggedIn()
        }
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
                            onItemClick = { content, genres ->
                                val params = Bundle()
                                if (content is Movie) {
                                    params.putString(Constants.SCREEN_NAME_KEY, content.title)
                                }
                                params.putSerializable(MOVIE_KEY, content)
                                params.putSerializable(
                                    MOVIE_GENRES_KEY,
                                    genres as Serializable
                                )
                                gotoMovie(params)
                            },
                            onSeeAllClick = {
                                context?.let {
//                                    ContentsActivity.startActivity(it, movies, R.string.movies)
                                }
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
//                                ContentsActivity.startActivity(itemView.context, content, titleRes)
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
        val params = Bundle()
        params.putString(Constants.SCREEN_NAME_KEY, movie.title)
        params.putSerializable(PEOPLE_KEY, person)
        params.putBoolean(MOVIE_TEAM_KEY, true)
        val intent = Intent(activity, PersonFragment::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }

    private fun gotoMovie(params: Bundle) {
        val intent = Intent(activity, MovieFragment::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}
