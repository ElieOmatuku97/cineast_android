package elieomatuku.cineast_android.presentation.details.movie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.presentation.contents.ContentsActivity
import elieomatuku.cineast_android.presentation.details.BareOverviewWidget
import elieomatuku.cineast_android.presentation.details.DetailTabs
import elieomatuku.cineast_android.presentation.details.movie.movie_staff.MovieStaffWidget
import elieomatuku.cineast_android.presentation.details.movie.overview.MovieOverviewWidget
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.FavoriteState
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.domain.model.Trailer
import elieomatuku.cineast_android.domain.model.WatchListState
import elieomatuku.cineast_android.presentation.extensions.asListOfType
import elieomatuku.cineast_android.presentation.fragment.RateDialogFragment
import elieomatuku.cineast_android.presentation.materialtheme.ui.theme.AppTheme
import elieomatuku.cineast_android.presentation.base.BaseFragment
import elieomatuku.cineast_android.presentation.utils.ContentUtils
import elieomatuku.cineast_android.presentation.utils.UiUtils
import elieomatuku.cineast_android.presentation.widgets.EmptyStateWidget
import elieomatuku.cineast_android.presentation.widgets.LoadingIndicatorWidget
import elieomatuku.cineast_android.presentation.widgets.movieswidget.MoviesWidget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class MovieFragment : BaseFragment() {
    private var isInWatchList: Boolean = false
    private var isInFavoriteList: Boolean = false
    private lateinit var menuHost: MenuHost

    private val watchListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create()
    }
    private val favoriteListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create()
    }

    private val viewModel by viewModels<MovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    MovieScreen(
                        hasNetworkConnection = connectionService.hasNetworkConnection,
                        goToGallery = { goToGallery() },
                        goToWebsite = { goToWebsite(it) },
                        gotoPerson = { gotoPerson(it) },
                        gotoMovie = { gotoMovie(it) },
                        showRatingDialog = { showRatingDialog(it) },
                        showTrailer = { showTrailer(it) },
                        onSeeAllClick = {
                            it.asListOfType<Movie>()?.let { movies ->
                                ContentsActivity.startActivity(
                                    requireContext(),
                                    movies,
                                    R.string.movies
                                )
                            }

                            it.asListOfType<Person>()?.let { people ->
                                ContentsActivity.startActivity(
                                    requireContext(),
                                    people,
                                    R.string.people
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.item_menu, menu)

                menu.findItem(R.id.action_share).apply {
                    isVisible = ContentUtils.supportsShare(viewModel.viewState.value?.movie?.id)
                }

                menu.findItem(R.id.action_watchlist).apply {
                    isChecked = isInWatchList
                    updateWatchListIcon(this)
//                    isVisible = viewModel.isLoggedIn()
                }

                menu.findItem(R.id.action_favorites).apply {
                    isChecked = isInFavoriteList
                    updateFavoriteListIcon(this)
//                    isVisible = viewModel.isLoggedIn()
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

        viewModel.viewState.observe(viewLifecycleOwner) {
            updateView(it)
        }
    }

    override fun onResume() {
        rxSubs.add(
            watchListCheckPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { event: Boolean ->
//                        updateWatchList(event)
                    },
                    {}
                )
        )

        rxSubs.add(
            favoriteListCheckPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { event: Boolean ->
//                        updateFavorite(event)
                    },
                    {}
                )
        )
        super.onResume()
    }

    private fun updateView(movieViewState: MovieViewState) {
        updateWatchList(movieViewState.movie?.watchListState)
        updateFavorite(movieViewState.movie?.favoritesState)
    }

    private fun updateWatchList(watchListState: WatchListState?) {
        isInWatchList = watchListState?.isSelected ?: false
        menuHost.invalidateMenu()
    }

    private fun updateFavorite(favoriteState: FavoriteState?) {
        isInFavoriteList = favoriteState?.isSelected ?: false
        menuHost.invalidateMenu()
    }

    private fun onShareMenuClicked() {
        val shareIntent: Intent? =
            UiUtils.getShareIntent(
                viewModel.viewState.value?.movie?.title,
                viewModel.viewState.value?.movie?.id
            )
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
        }
    }

    private fun goToGallery() {
        val directions =
            MovieFragmentDirections.navigateToGallery(
                viewModel.posters()
                    .map { it.filePath }
                    .toTypedArray()
            )
        findNavController().navigate(directions)
    }

    private fun gotoPerson(person: Content) {
        if (person is Person) {
            val directions = MovieFragmentDirections.navigateToPersonDetail(
                viewModel.viewState.value?.movie?.title ?: String(),
                person.id
            )
            findNavController().navigate(directions)
        }
    }

    private fun gotoMovie(content: Content) {
        if (content is Movie) {
            val directions = MovieFragmentDirections.navigateToMovieDetail(
                viewModel.viewState.value?.movie?.title ?: String(),
                content.id
            )
            findNavController().navigate(directions)
        }
    }

    private fun goToWebsite(url: String) {
        val directions = MovieFragmentDirections.navigateToWebsite(url)
        findNavController().navigate(directions)
    }

    private fun showTrailer(trailerKey: String) {
        val directions = MovieFragmentDirections.navigateToVideo(trailerKey)
        findNavController().navigate(directions)
    }

    private fun showRatingDialog(movieSummary: MovieSummary) {
        val rateDialogFragment =
            RateDialogFragment.newInstance(movieSummary.movie)
        if (context is AppCompatActivity) {
            rateDialogFragment.show(
                (context as AppCompatActivity).supportFragmentManager,
                RateDialogFragment.TAG
            )
        }
    }
}

@Composable
fun MovieScreen(
    viewModel: MovieViewModel = hiltViewModel(),
    hasNetworkConnection: Boolean,
    goToGallery: () -> Unit,
    goToWebsite: (String) -> Unit,
    gotoPerson: (Person) -> Unit,
    gotoMovie: (Movie) -> Unit,
    showRatingDialog: (MovieSummary) -> Unit,
    showTrailer: (String) -> Unit,
    onSeeAllClick: (List<Content>) -> Unit,
) {
    val viewState by viewModel.viewState.observeAsState()

    viewState?.apply {
        Box(modifier = Modifier.fillMaxSize()) {
            movie?.movieSummary?.let { movieSummary ->
                Column {
                    MovieProfile(
                        movieSummary = movieSummary,
                        onProfileClick = {
                            goToGallery()
                        },
                        onRateClick = {
                            showRatingDialog(movieSummary)
                        },
                        gotoLink = {
                            goToWebsite(it)
                        }
                    )

                    movie.apply {
                        MovieTabs(
                            movieSummary = movieSummary,
                            movie = this,
                            onSeeAllClick = {
                                onSeeAllClick(it)
                            },
                            onItemClick = {
                                if (it is Person) {
                                    gotoPerson(it)
                                }

                                if (it is Movie) {
                                    gotoMovie(it)
                                }
                            },
                            onTrailerClick = { trailer ->
                                trailer?.key?.let {
                                    showTrailer(it)
                                }
                            }
                        )
                    }
                }
            }

            viewError?.consume()?.apply {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EmptyStateWidget(
                        errorMsg = message,
                        hasNetworkConnection = hasNetworkConnection
                    )
                }
            }

            if (isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingIndicatorWidget()
                }
            }
        }
    }
}

@Composable
fun MovieTabs(
    movieSummary: MovieSummary,
    movie: Movie,
    onSeeAllClick: (List<Content>) -> Unit,
    onItemClick: (Content) -> Unit,
    onTrailerClick: (Trailer?) -> Unit
) {
    val tabs by lazy {
        listOf(
            R.string.overview,
            R.string.people,
            R.string.similar
        )
    }

    DetailTabs(tabs = tabs) {
        when (it) {
            R.string.overview -> {
                MovieOverviewWidget(
                    overviewTitle = stringResource(R.string.plot_summary),
                    movieSummary = movieSummary,
                    onTrailerClick = { trailer ->
                        onTrailerClick(trailer)
                    }) { title, overview ->
                    BareOverviewWidget(title = title, overview = overview)
                }
            }

            R.string.people -> {
                val cast = movieSummary.cast
                val crew = movieSummary.crew
                if (cast != null && crew != null) {
                    MovieStaffWidget(
                        cast = cast,
                        crew = crew,
                        onItemClick = {
                            onItemClick(it)
                        }
                    ) {
                        onSeeAllClick(it)
                    }
                }
            }

            R.string.similar -> {
                val similarMovies: List<Movie> = movieSummary.similarMovies ?: listOf()
                MoviesWidget(
                    movies = similarMovies,
                    genres = emptyList(),
                    sectionTitle = movie.title ?: stringResource(R.string.movies),
                    onItemClick = { content, _ ->
                        onItemClick(content)
                    },
                    onSeeAllClick = { movies ->
                        onSeeAllClick(movies)
                    }
                )
            }
        }
    }
}