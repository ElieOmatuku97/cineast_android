package elieomatuku.cineast_android.details.person

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.contents.ContentsActivity
import elieomatuku.cineast_android.details.BareOverviewWidget
import elieomatuku.cineast_android.details.DetailTabs
import elieomatuku.cineast_android.details.Profile
import elieomatuku.cineast_android.details.movie.MovieFragmentDirections
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.PersonDetails
import elieomatuku.cineast_android.extensions.asListOfType
import elieomatuku.cineast_android.materialtheme.ui.theme.AppTheme
import elieomatuku.cineast_android.utils.ContentUtils
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.widgets.EmptyStateWidget
import elieomatuku.cineast_android.widgets.LoadingIndicatorWidget
import elieomatuku.cineast_android.widgets.movieswidget.MoviesWidget

class PersonFragment : BaseFragment() {

    private lateinit var menuHost: MenuHost

    private val viewModel: PersonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    PersonScreen(
                        hasNetworkConnection = connectionService.hasNetworkConnection,
                        goToGallery = { navigateToGallery() },
                        goToWebsite = {
                            goToWebsite(it)
                        },
                        gotoMovie = {
                            gotoMovie(it)
                        },
                        onSeeAllClick = {
                            it.asListOfType<Movie>()?.let { movies ->
                                ContentsActivity.startActivity(
                                    requireContext(),
                                    movies,
                                    R.string.movies
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
                menu.findItem(R.id.action_share)?.apply {
                    isVisible = ContentUtils.supportsShare(viewModel.person?.id)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_share -> {
                        onShareMenuClicked()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.viewState.observe(viewLifecycleOwner) {
            updateActionShare()
        }
    }

    private fun updateActionShare() {
        menuHost.invalidateMenu()
    }

    private fun onShareMenuClicked() {
        val path = "person"
        val shareIntent: Intent? =
            UiUtils.getShareIntent(viewModel.person?.name, viewModel.person?.id, path)

        // Make sure there is an activity that supports the intent
        if (shareIntent?.resolveActivity(requireContext().packageManager) != null) {
            startActivity(
                Intent.createChooser(
                    shareIntent,
                    getString(R.string.share_title)
                )
            )
        }
    }

    private fun navigateToGallery() {
        val directions =
            PersonFragmentDirections.navigateToGallery(
                viewModel.posters
                    .map { it.filePath }
                    .toTypedArray()
            )
        findNavController().navigate(directions)
    }

    private fun gotoMovie(content: Content) {
        if (content is Movie) {
            val directions = MovieFragmentDirections.navigateToMovieDetail(
                content.title ?: String(),
                content.id
            )
            findNavController().navigate(directions)
        }
    }

    private fun goToWebsite(url: String) {
        val directions = PersonFragmentDirections.navigateToWebsite(url)
        findNavController().navigate(directions)
    }
}

@Composable
fun PersonScreen(
    viewModel: PersonViewModel = hiltViewModel(),
    hasNetworkConnection: Boolean,
    goToGallery: () -> Unit,
    goToWebsite: (String) -> Unit,
    gotoMovie: (Movie) -> Unit,
    onSeeAllClick: (List<Content>) -> Unit,
) {
    val viewState by viewModel.viewState.observeAsState()

    viewState?.apply {
        Box(modifier = Modifier.fillMaxSize()) {
            person?.let { person ->
                Column {
                    Profile(
                        imagePath = person.profilePath,
                        title = person.name,
                        subTitle = person.birthday,
                        description = person.placeOfBirth,
                        webSiteLink = person.homepage,
                        onProfileClick = {
                            goToGallery()
                        },
                        onWebSiteLinkClick = {
                            goToWebsite(it)
                        }
                    )

                    PersonTabs(
                        person = person,
                        onSeeAllClick = {
                            onSeeAllClick(it)
                        },
                        movies = viewModel.knownForMovies,
                        onItemClick = {
                            if (it is Movie) {
                                gotoMovie(it)
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
fun PersonTabs(
    person: PersonDetails,
    movies: List<Movie>,
    onSeeAllClick: (List<Content>) -> Unit,
    onItemClick: (Content) -> Unit,
) {
    val tabs by lazy {
        listOf(
            R.string.overview,
            R.string.known_for
        )
    }

    DetailTabs(tabs = tabs) {
        when (it) {
            R.string.overview -> {
                BareOverviewWidget(
                    title = stringResource(R.string.biography),
                    overview = person.biography ?: String()
                )
            }
            R.string.known_for -> {
                MoviesWidget(
                    movies = movies,
                    genres = emptyList(),
                    sectionTitle = stringResource(R.string.cast),
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
