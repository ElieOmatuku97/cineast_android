package elieomatuku.cineast_android.presentation.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import elieomatuku.cineast_android.presentation.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.presentation.materialtheme.ui.theme.AppTheme
import elieomatuku.cineast_android.presentation.base.BaseFragment
import elieomatuku.cineast_android.presentation.contents.ContentsActivity
import elieomatuku.cineast_android.presentation.search.movie.MoviesGrid
import elieomatuku.cineast_android.presentation.search.people.PeopleGrid
import elieomatuku.cineast_android.presentation.widgets.LoadingIndicatorWidget
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment() {

    private val viewModel: SearchViewModel by viewModels()

    val searchQueryPublisher: PublishSubject<String> by lazy {
        PublishSubject.create()
    }
    private val searchQueryObservable: Observable<String>
        get() = searchQueryPublisher.hide()

    private lateinit var composeView: ComposeView

    private var isMovieSearchScreen: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        composeView = ComposeView(requireContext())
        composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configMenu()

        composeView.setContent {
            AppTheme {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    SearchPager(
                        hasNetworkConnection = connectionService.hasNetworkConnection,
                        onContentClick = {
                            if (it is Movie) {
                                gotoMovie(it)
                            } else if (it is Person) {
                                gotoPerson(it)
                            }
                        }
                    )
                }
            }
        }

        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            if (state.results.isNotEmpty()) {
                showSearchResults(state.results)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        rxSubs.add(
            searchQueryObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { argQuery ->
                    if (isMovieSearchScreen) {
                        viewModel.searchMovies(argQuery)
                    } else {
                        viewModel.searchPeople(argQuery)
                    }
                }
        )
    }

    private fun showSearchResults(results: List<Content>) {
        ContentsActivity.startActivity(requireActivity(), results, R.string.search_hint)
    }

    private fun gotoMovie(movie: Movie) {
        val directions = SearchFragmentDirections.navigateToMovieDetail(
            getString(R.string.nav_title_search),
            movie.id
        )
        findNavController().navigate(directions)
    }

    private fun gotoPerson(person: Person) {
        val directions = SearchFragmentDirections.navigateToPersonDetail(
            getString(R.string.nav_title_search),
            person.id
        )
        findNavController().navigate(directions)
    }

    private fun configMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)

                val searchManager =
                    (activity)?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
                (menu.findItem(R.id.menu_action_search).actionView as SearchView).apply {
                    setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
                    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextChange(newText: String?): Boolean {
                            return true
                        }

                        override fun onQueryTextSubmit(query: String?): Boolean {
                            query?.let {
                                this@SearchFragment.searchQueryPublisher.onNext(it)
                            }
                            return false
                        }
                    })
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SearchPager(
    hasNetworkConnection: Boolean,
    viewModel: SearchViewModel = hiltViewModel(),
    onContentClick: (content: Content) -> Unit
) {
    val viewState by viewModel.viewState.observeAsState()

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val pages: List<Int> by lazy {
        listOf(
            R.string.movies,
            R.string.people
        )
    }

    viewState?.apply {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                ) {
                    pages.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(stringResource(id = title).uppercase()) },
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                        )
                    }
                }

                HorizontalPager(
                    count = pages.size,
                    state = pagerState
                ) { page ->
                    when (pages[page]) {
                        R.string.movies -> {
                            MoviesGrid(
                                hasNetworkConnection = hasNetworkConnection
                            ) {
                                onContentClick(it)
                            }
                        }

                        R.string.people -> {
                            PeopleGrid(
                                hasNetworkConnection = hasNetworkConnection
                            ) {
                                onContentClick(it)
                            }
                        }
                    }
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