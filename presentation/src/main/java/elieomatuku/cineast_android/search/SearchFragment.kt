package elieomatuku.cineast_android.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.connection.ConnectionService
import elieomatuku.cineast_android.contents.ContentsActivity
import elieomatuku.cineast_android.search.movie.MoviesGrid
import elieomatuku.cineast_android.search.people.PeopleGrid
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class SearchFragment : BaseFragment() {
    private val viewModel: SearchViewModel by viewModel()

    val searchQueryPublisher: PublishSubject<String> by lazy {
        PublishSubject.create()
    }
    private val searchQueryObservable: Observable<String>
        get() = searchQueryPublisher.hide()

    private lateinit var composeView: ComposeView
    private val connectionService: ConnectionService by instance()

    var isMovieSearchScreen: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.setContent {
            SearchPager(
                viewModelFactory = viewModelFactory,
                hasNetworkConnection = connectionService.hasNetworkConnection
            ) {
                isMovieSearchScreen = it == 0
            }
        }

        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                showLoading(requireView())
            } else {
                hideLoading(requireView())
            }

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
                    showLoading(requireView())
                    if (isMovieSearchScreen) {
                        viewModel.searchMovies(argQuery)
                    } else {
                        viewModel.searchPeople(argQuery)
                    }
                }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = (activity)?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.menu_action_search)?.actionView as SearchView).apply {
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
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showSearchResults(results: List<Content>?) {
        results?.let {
            ContentsActivity.startActivity(requireActivity(), results, R.string.search_hint)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SearchPager(
    viewModelFactory: ViewModelProvider.Factory,
    hasNetworkConnection: Boolean,
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

                    }
                }
                R.string.people -> {
                    PeopleGrid(
                        viewModelFactory = viewModelFactory,
                        hasNetworkConnection = hasNetworkConnection
                    ) {

                    }
                }
            }
        }
    }
}