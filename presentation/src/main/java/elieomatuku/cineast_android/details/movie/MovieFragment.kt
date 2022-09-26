package elieomatuku.cineast_android.details.movie

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.contents.ContentsActivity
import elieomatuku.cineast_android.details.BareOverviewWidget
import elieomatuku.cineast_android.details.movie.movie_team.MovieTeamWidget
import elieomatuku.cineast_android.details.movie.overview.MovieOverviewWidget
import elieomatuku.cineast_android.domain.model.*
import elieomatuku.cineast_android.extensions.asListOfType
import elieomatuku.cineast_android.fragment.RateDialogFragment
import elieomatuku.cineast_android.utils.*
import elieomatuku.cineast_android.viewholder.EmptyStateItem
import elieomatuku.cineast_android.widgets.LoadingIndicatorWidget
import elieomatuku.cineast_android.widgets.MoviesWidget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class MovieFragment : BaseFragment() {
    lateinit var movie: Movie
    private var isInWatchList: Boolean = false
    private var isInFavoriteList: Boolean = false
    private val args: MovieFragmentArgs by navArgs()
    private lateinit var menuHost: MenuHost

    private val watchListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create()
    }
    private val favoriteListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create()
    }

    private val viewModel: MovieViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppCompatTheme {
                    MovieScreen(
                        viewModelFactory = viewModelFactory,
                        viewModel = viewModel,
                        movie = movie,
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

        movie = args.movie
        viewModel.getMovieDetails(movie, args.screenName)
        viewModel.viewState.observe(viewLifecycleOwner) {
            updateView(it)
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
                    {}
                )
        )

        rxSubs.add(
            favoriteListCheckPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { event: Boolean ->
                        updateFavorite(event)
                    },
                    {}
                )
        )
        super.onResume()
    }

    private fun updateView(movieViewState: MovieViewState) {
        updateWatchList(movieViewState.isInWatchList)
        updateFavorite(movieViewState.isInFavorites)
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
    viewModelFactory: ViewModelProvider.Factory,
    viewModel: MovieViewModel = viewModel(factory = viewModelFactory),
    movie: Movie,
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
            movieSummary?.let { movieSummary ->
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

                    MovieTabs(
                        viewModelFactory = viewModelFactory,
                        movieSummary = movieSummary,
                        movie = movie,
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

            viewError?.consume()?.apply {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EmptyStateItem(
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
    viewModelFactory: ViewModelProvider.Factory,
    movieSummary: MovieSummary,
    movie: Movie,
    onSeeAllClick: (List<Content>) -> Unit,
    onItemClick: (Content) -> Unit,
    onTrailerClick: (Trailer?) -> Unit
) {
    var state by remember { mutableStateOf(0) }
    val tabs: List<Int> by lazy {
        listOf(
            R.string.overview,
            R.string.people,
            R.string.similar
        )
    }
    Column {
        TabRow(
            selectedTabIndex = state,
            contentColor = colorResource(id = R.color.color_orange_app),
            backgroundColor = colorResource(id = R.color.color_black_app), 
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(stringResource(id = title).uppercase()) },
                    selected = state == index,
                    onClick = {
                        state = index
                    },
                    selectedContentColor = colorResource(id = R.color.color_orange_app),
                    unselectedContentColor = colorResource(id = R.color.color_grey_app),
                )
            }
        }

        when (tabs[state]) {
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
                    MovieTeamWidget(
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
                    viewModelFactory = viewModelFactory,
                    movies = similarMovies,
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