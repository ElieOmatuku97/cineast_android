package elieomatuku.cineast_android.discover

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.fragment.findNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import elieomatuku.cineast_android.AppTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.contents.ContentsActivity
import elieomatuku.cineast_android.domain.model.*
import elieomatuku.cineast_android.extensions.DiscoverWidget
import elieomatuku.cineast_android.extensions.asListOfType
import elieomatuku.cineast_android.extensions.getWidgets
import elieomatuku.cineast_android.utils.*
import elieomatuku.cineast_android.widgets.EmptyStateWidget
import elieomatuku.cineast_android.widgets.LoadingIndicatorWidget
import elieomatuku.cineast_android.widgets.PeopleWidget
import elieomatuku.cineast_android.widgets.movieswidget.MoviesWidget

class DiscoverFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    DiscoverScreen(
                        hasNetworkConnection = connectionService.hasNetworkConnection,
                        onSeeAllClick = { contents, titleResources ->
                            ContentsActivity.startActivity(
                                requireContext(),
                                contents,
                                titleResources
                            )
                        },
                        gotoMovie = ::gotoMovie,
                        gotoPerson = ::gotoPerson,
                        gotoWebView = ::gotoWebView
                    )
                }
            }
        }
    }

    private fun gotoWebView(accessToken: AccessToken?) {
        accessToken?.let {
            val authenticateUrl = Uri.parse(resources.getString(R.string.authenticate_url))
                .buildUpon()
                .appendPath(it.requestToken)
                .build()
                .toString()
            val directions = DiscoverFragmentDirections.navigateToLogin(authenticateUrl)
            findNavController().navigate(directions)
        }
    }

    private fun gotoMovie(movie: Movie) {
        val directions = DiscoverFragmentDirections.navigateToMovieDetail(
            getString(R.string.nav_title_discover),
            movie.id
        )
        findNavController().navigate(directions)
    }

    private fun gotoPerson(person: Person) {
        val directions = DiscoverFragmentDirections.navigateToPersonDetail(
            getString(R.string.nav_title_discover),
            person.id
        )
        findNavController().navigate(directions)
    }
}

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel = hiltViewModel(),
    hasNetworkConnection: Boolean,
    onSeeAllClick: (contents: List<Content>, titleResources: Int) -> Unit,
    gotoMovie: (movie: Movie) -> Unit,
    gotoPerson: (person: Person) -> Unit,
    gotoWebView: (accessToken: AccessToken) -> Unit
) {
    val viewState by viewModel.viewState.observeAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    viewState?.accessToken.consume {
        gotoWebView(it)
    }

    viewState?.apply {
        Scaffold {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
//                    modifier = Modifier.background(colorResource(id = R.color.color_black_app)),
                    onRefresh = {
                        viewModel.refresh()
                    }
                ) {
                    viewError?.apply {
                        val errorMessage = peek().message
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            EmptyStateWidget(
                                errorMsg = errorMessage,
                                hasNetworkConnection = hasNetworkConnection
                            )
                        }
                    }

                    discoverContents?.apply {
                        LazyColumn {
                            items(getWidgets()) { widget ->
                                when (widget) {
                                    is DiscoverWidget.Header -> {
                                        LazyRow(
                                            modifier = Modifier
                                                .height(dimensionResource(id = R.dimen.layout_height_xlarge))
                                        ) {
                                            items(
                                                widget.value.asListOfType<Movie>()
                                                    ?: emptyList()
                                            ) { movie ->
                                                HeaderItem(movie = movie) {
                                                    gotoMovie(it)
                                                }
                                            }
                                        }
                                    }
                                    is DiscoverWidget.People -> {
                                        PeopleWidget(
                                            people = widget.value.asListOfType()
                                                ?: emptyList(),
                                            sectionTitle = stringResource(widget.titleResources),
                                            onItemClick = {
                                                if (it is Person) {
                                                    gotoPerson(it)
                                                }
                                            }
                                        ) {
                                            onSeeAllClick(it, widget.titleResources)
                                        }
                                        Divider()
                                    }
                                    is DiscoverWidget.Movies -> {
                                        MoviesWidget(
                                            movies = widget.value.asListOfType()
                                                ?: emptyList(),
                                            genres = emptyList(),
                                            sectionTitle = stringResource(widget.titleResources),
                                            onItemClick = { content, _ ->
                                                if (content is Movie) {
                                                    gotoMovie(content)
                                                }
                                            },
                                            onSeeAllClick = {
                                                onSeeAllClick(it, widget.titleResources)
                                            }
                                        )
                                        Divider()
                                    }
                                    is DiscoverWidget.Login -> {
                                        LoginItem(
                                            isLoggedIn = viewState?.isLoggedIn ?: false
                                        ) {
                                            if (!viewModel.isLoggedIn()) {
                                                viewModel.logIn()
                                            } else {
                                                viewModel.logout()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (isLoading) {
                    LoadingIndicatorWidget()
                }
            }
        }
    }
}
