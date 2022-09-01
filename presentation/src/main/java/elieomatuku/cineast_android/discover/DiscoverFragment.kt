package elieomatuku.cineast_android.discover

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.*
import elieomatuku.cineast_android.extensions.getWidgets
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.connection.ConnectionService
import elieomatuku.cineast_android.contents.ContentsActivity
import elieomatuku.cineast_android.extensions.DiscoverWidget
import elieomatuku.cineast_android.extensions.asListOfType
import elieomatuku.cineast_android.settings.LoginWebViewFragment
import elieomatuku.cineast_android.utils.*
import elieomatuku.cineast_android.viewholder.EmptyStateItem
import elieomatuku.cineast_android.widgets.MoviesWidget
import elieomatuku.cineast_android.widgets.PeopleWidget
import org.kodein.di.generic.instance

class DiscoverFragment : BaseFragment() {

    private val viewModel: DiscoverViewModel by viewModel()
    private val connectionService: ConnectionService by instance()
    private lateinit var composeView: ComposeView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        composeView = ComposeView(requireContext())
        return composeView.apply {
            setContent {
                DiscoverScreen(
                    viewModelFactory = viewModelFactory,
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                showLoading(requireView())
            } else {
                hideLoading(requireView())
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

            val fm = (activity as AppCompatActivity).supportFragmentManager

            fm.beginTransaction()
                .add(android.R.id.content, LoginWebViewFragment.newInstance(authenticateUrl), null)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun gotoMovie(movie: Movie) {
        val directions = DiscoverFragmentDirections.navigateToMovieDetail(
            getString(R.string.nav_title_discover),
            movie
        )
        findNavController().navigate(directions)
    }

    private fun gotoPerson(person: Person) {
        val directions = DiscoverFragmentDirections.navigateToPersonDetail(
            getString(R.string.nav_title_discover),
            person
        )
        findNavController().navigate(directions)
    }
}

@Composable
fun DiscoverScreen(
    viewModelFactory: ViewModelProvider.Factory,
    viewModel: DiscoverViewModel = viewModel(factory = viewModelFactory),
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

    viewState?.viewError?.apply {
        EmptyStateItem(
            errorMsg = this.peek().message,
            hasNetworkConnection = hasNetworkConnection
        )
    }

    viewState?.discoverContents?.apply {
        Scaffold(
            topBar = {
                Box {
                    val drawable =
                        AppCompatResources.getDrawable(
                            LocalContext.current,
                            R.drawable.bg_actionbar
                        )
                    Image(
                        painter = rememberDrawablePainter(drawable = drawable),
                        contentDescription = null,
                        modifier = Modifier
                            .matchParentSize()
                    )
                    TopAppBar(
                        title = {
                            Text(
                                stringResource(R.string.nav_title_discover),
                                style = TextStyle(
                                    fontSize = dimensionResource(id = R.dimen.toolbar_text_size).value.sp,
                                    color = colorResource(id = R.color.color_white),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        backgroundColor = Color.Transparent
                    )
                }
            }
        ) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                modifier = Modifier.background(colorResource(id = R.color.color_black_app)),
                onRefresh = {
                    viewModel.refresh()
                }
            ) {
                LazyColumn {
                    items(getWidgets()) { widget ->
                        when (widget) {
                            is DiscoverWidget.Header -> {
                                LazyRow(
                                    modifier = Modifier
                                        .height(dimensionResource(id = R.dimen.holder_header_item_height))
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
                                    people = widget.value.asListOfType() ?: emptyList(),
                                    sectionTitle = stringResource(widget.titleResources),
                                    onItemClick = {
                                        if (it is Person) {
                                            gotoPerson(it)
                                        }
                                    }
                                ) {
                                    onSeeAllClick(it, widget.titleResources)
                                }
                                Divider(color = colorResource(id = R.color.color_grey_app))
                            }
                            is DiscoverWidget.Movies -> {
                                MoviesWidget(
                                    viewModelFactory = viewModelFactory,
                                    movies = widget.value.asListOfType() ?: emptyList(),
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
                                Divider(color = colorResource(id = R.color.color_grey_app))
                            }
                            is DiscoverWidget.Login -> {
                                LoginItem(isLoggedIn = viewState?.isLoggedIn ?: false) {
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
    }
}
