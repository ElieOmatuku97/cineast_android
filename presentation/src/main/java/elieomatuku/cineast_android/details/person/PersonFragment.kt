package elieomatuku.cineast_android.details.person

import android.content.Intent
import android.os.Bundle
import android.view.*
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
import elieomatuku.cineast_android.details.DetailTabs
import elieomatuku.cineast_android.details.Profile
import elieomatuku.cineast_android.details.movie.MovieFragmentDirections
import elieomatuku.cineast_android.domain.model.*
import elieomatuku.cineast_android.extensions.asListOfType
import elieomatuku.cineast_android.utils.ContentUtils
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.viewholder.EmptyStateWidget
import elieomatuku.cineast_android.widgets.LoadingIndicatorWidget
import elieomatuku.cineast_android.widgets.movieswidget.MoviesWidget

class PersonFragment : BaseFragment() {
    private val viewModel: PersonViewModel by viewModel()
    private lateinit var menuHost: MenuHost
    private val args: PersonFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppCompatTheme {
                    PersonScreen(
                        viewModelFactory = viewModelFactory,
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
                    UiUtils.tintMenuItem(this, requireContext(), R.color.color_orange_app)
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

        val person = args.person
        viewModel.getPersonDetails(person)
        viewModel.getKnownForMovies(person)
        viewModel.getImages(person)

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
    viewModelFactory: ViewModelProvider.Factory,
    viewModel: PersonViewModel = viewModel(factory = viewModelFactory),
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
                        viewModelFactory = viewModelFactory,
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
    viewModelFactory: ViewModelProvider.Factory,
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
                    viewModelFactory = viewModelFactory,
                    movies = movies,
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
