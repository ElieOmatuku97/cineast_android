package elieomatuku.cineast_android.details.movie

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.databinding.FragmentContentDetailsBinding
import elieomatuku.cineast_android.details.MoviesFragment
import elieomatuku.cineast_android.details.movie.MovieFragmentArgs
import elieomatuku.cineast_android.details.gallery.GalleryFragment
import elieomatuku.cineast_android.details.movie.movie_team.MovieTeamFragment
import elieomatuku.cineast_android.details.movie.overview.MovieOverviewFragment
import elieomatuku.cineast_android.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_content_details.*
import timber.log.Timber
import java.io.Serializable

class MovieFragment : BaseFragment(R.layout.fragment_content_details) {
    companion object {
        const val MOVIE_OVERVIEW = "overview"
        const val MOVIE_CREW = "crew"
        const val SIMILAR_MOVIES = "similar_movies"
    }

    lateinit var movie: Movie
    private var isInWatchList: Boolean = false
    private var isInFavoriteList: Boolean = false

    private lateinit var binding: FragmentContentDetailsBinding
    private val args: MovieFragmentArgs by navArgs()

    private val watchListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
    }

    private val favoriteListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
    }

    private val viewModel: MovieViewModel by viewModel<MovieViewModel>()

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
        binding = FragmentContentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListView()
        toolbar?.let {
            UiUtils.initToolbar(requireActivity() as AppCompatActivity, it,true)
        }

        val screenName = args.screenName
        movie = args.movie
        viewModel.getMovieDetails(movie, screenName)

        viewModel.viewState.observe(viewLifecycleOwner) {

            if (it.isLoading) {
                showLoading(rootView)
            } else {
                hideLoading(rootView)
            }

            toolbar?.title = it.screenName

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
                    goToGallery()
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

//    override fun onSupportNavigateUp(): Boolean {
//        if (!super.onSupportNavigateUp()) {
//            onBackPressed()
//        }
//
//        return true
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        this.menuInflater.inflate(R.menu.item_menu, menu)
//        if (menu != null) {
//            menu.findItem(R.id.action_share)?.let {
//                val colorRes = R.color.color_orange_app
//                UiUtils.tintMenuItem(it, this, colorRes)
//            }
//        }
//        return true
//    }

//    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
//        menu?.findItem(R.id.action_share)?.let {
//            it.isVisible = ContentUtils.supportsShare(movie.id)
//        }
//
//        menu?.findItem(R.id.action_watchlist)?.let {
//            val menuItem = it
//            menuItem.isChecked = isInWatchList
//
//            updateWatchListIcon(it)
//            it.isVisible = viewModel.isLoggedIn()
//        }
//
//        menu?.findItem(R.id.action_favorites)?.let {
//            val menuItem = it
//            menuItem.isChecked = isInFavoriteList
//            updateFavoriteListIcon(it)
//            it.isVisible = viewModel.isLoggedIn()
//        }
//
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            android.R.id.home -> onSupportNavigateUp()
//
//            R.id.action_share -> {
//                onShareMenuClicked()
//            }
//
//            R.id.action_watchlist -> {
//                onWatchListMenuClicked(item)
//            }
//
//            R.id.action_favorites -> {
//                onFavoriteListMenuClicked(item)
//            }
//
//            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun updateWatchList(event: Boolean) {
//        isInWatchList = event
//        invalidateOptionsMenu()
    }

    private fun updateFavorite(event: Boolean) {
//        isInFavoriteList = event
//        invalidateOptionsMenu()
    }

//    private fun onShareMenuClicked() {
//        val shareIntent: Intent? = UiUtils.getShareIntent(movie.title, movie.id)
//        // Make sure there is an activity that supports the intent
//        if (shareIntent?.resolveActivity(packageManager) != null) {
//            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
//        }
//    }

    private fun onWatchListMenuClicked(item: MenuItem) {
//        item.isChecked = !item.isChecked
//        val checked = item.isChecked
//        updateWatchListIcon(item)
//
//        if (checked) {
//            viewModel.addMovieToWatchList()
//        } else {
//            viewModel.removeMovieFromWatchList()
//        }
    }

//    private fun updateWatchListIcon(item: MenuItem) {
//        val colorRes = R.color.color_orange_app
//        if (item.isChecked) {
//            item.icon =
//                ResourcesCompat.getDrawable(resources, R.drawable.ic_nav_watch_list_selected, theme)
//        } else {
//            item.icon = ResourcesCompat.getDrawable(
//                resources,
//                R.drawable.ic_nav_watch_list_unselected,
//                theme
//            )
//            UiUtils.tintMenuItem(item, this, colorRes)
//        }
//    }

    private fun onFavoriteListMenuClicked(item: MenuItem) {
//        item.isChecked = !item.isChecked
//        val checked = item.isChecked
//        updateFavoriteListIcon(item)
//
//        if (checked) {
//            viewModel.addMovieToFavoriteList()
//        } else {
//            viewModel.removeMovieFromFavoriteList()
//        }
    }

//    private fun updateFavoriteListIcon(item: MenuItem) {
//        val colorRes = R.color.color_orange_app
//        if (item.isChecked) {
//            item.icon =
//                ResourcesCompat.getDrawable(resources, R.drawable.ic_star_black_selected, theme)
//        } else {
//            item.icon = ResourcesCompat.getDrawable(
//                resources,
//                R.drawable.ic_star_border_black_unselected,
//                theme
//            )
//            UiUtils.tintMenuItem(item, this, colorRes)
//        }
//    }

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

        val overViewFragment = MovieOverviewFragment.newInstance(
            getString(R.string.plot_summary),
            movieSummary
        )
        updateContainer(overViewFragment)
    }

    private fun goToGallery() {
        val galleryFragment = GalleryFragment.newInstance()
        val args = Bundle()
        args.putSerializable(
            GalleryFragment.POSTERS,
            viewModel.posters() as Serializable
        )
        galleryFragment.arguments = args

        childFragmentManager.beginTransaction()
            .add(android.R.id.content, galleryFragment, null).addToBackStack(null).commit()
    }

    private fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
        binding.listViewContainer.visibility = View.VISIBLE
    }

    private fun gotoTab(displayAndMovieSummary: Pair<String, MovieSummary>) {
        val fragment = when (displayAndMovieSummary.first) {
            SIMILAR_MOVIES -> {
                val movieSummary: MovieSummary = displayAndMovieSummary.second
                val similarMovies: List<Movie> = movieSummary.similarMovies ?: listOf()
                MoviesFragment.newInstance(similarMovies)
            }
            MOVIE_CREW -> {
                MovieTeamFragment.newInstance(displayAndMovieSummary.second)
            }
            MOVIE_OVERVIEW -> {
                MovieOverviewFragment.newInstance(
                    getString(R.string.plot_summary),
                    displayAndMovieSummary.second
                )
            }
            else -> null
        }

        if (fragment != null) {
            updateContainer(fragment)
        }
    }

    private fun updateContainer(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment).commit()
    }
}
