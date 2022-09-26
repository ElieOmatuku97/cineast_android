package elieomatuku.cineast_android.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.contents.ContentsActivity
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.search.movie.MoviesGrid
import elieomatuku.cineast_android.search.people.PeopleGrid
import elieomatuku.cineast_android.widgets.LoadingIndicatorWidget
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment() {
    private val viewModel: SearchViewModel by viewModel()

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

        composeView.setContent {
            SearchPager(
                viewModelFactory = viewModelFactory,
                hasNetworkConnection = connectionService.hasNetworkConnection,
                onContentClick = {
                    if (it is Movie) {
                        gotoMovie(it)
                    } else if (it is Person) {
                        gotoPerson(it)
                    }
                }
            ) {
                isMovieSearchScreen = it == 0
            }
        }

        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            updateView(state)
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

    private fun updateView(state: SearchViewState) {
        composeView.setContent {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                SearchPager(
                    viewModelFactory = viewModelFactory,
                    hasNetworkConnection = connectionService.hasNetworkConnection,
                    onContentClick = {
                        if (it is Movie) {
                            gotoMovie(it)
                        } else if (it is Person) {
                            gotoPerson(it)
                        }
                    }
                ) {
                    isMovieSearchScreen = it == 0
                }

                if (state.isLoading) {
                    LoadingIndicatorWidget()
                }
            }
        }
    }

    private fun showSearchResults(results: List<Content>) {
        ContentsActivity.startActivity(requireActivity(), results, R.string.search_hint)
    }

    private fun gotoMovie(movie: Movie) {
        val directions = SearchFragmentDirections.navigateToMovieDetail(
            getString(R.string.nav_title_search),
            movie
        )
        findNavController().navigate(directions)
    }

    private fun gotoPerson(person: Person) {
        val directions = SearchFragmentDirections.navigateToPersonDetail(
            getString(R.string.nav_title_search),
            person
        )
        findNavController().navigate(directions)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SearchPager(
    viewModelFactory: ViewModelProvider.Factory,
    hasNetworkConnection: Boolean,
    onContentClick: (content: Content) -> Unit,
    updateCurrentPosition: (currentPosition: Int) -> Unit
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val pages: List<Int> by lazy {
        listOf(
            R.string.movies,
            R.string.people
        )
    }

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            contentColor = colorResource(id = R.color.color_orange_app),
            backgroundColor = colorResource(id = R.color.color_black_app),
            indicator = { tabPositions ->
                updateCurrentPosition(pagerState.currentPage)
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
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
                    selectedContentColor = colorResource(id = R.color.color_orange_app),
                    unselectedContentColor = colorResource(id = R.color.color_grey_app),
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
                        viewModelFactory = viewModelFactory,
                        hasNetworkConnection = hasNetworkConnection
                    ) {
                        onContentClick(it)
                    }
                }
                R.string.people -> {
                    PeopleGrid(
                        viewModelFactory = viewModelFactory,
                        hasNetworkConnection = hasNetworkConnection
                    ) {
                        onContentClick(it)
                    }
                }
            }
        }
    }
}