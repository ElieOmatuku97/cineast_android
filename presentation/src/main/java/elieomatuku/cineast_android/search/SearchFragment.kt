package elieomatuku.cineast_android.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.contents.ContentsActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.fragment_search_parent.*
import kotlinx.android.synthetic.main.fragment_search_parent.view.*

class SearchFragment : BaseFragment(R.layout.fragment_search_parent) {
    companion object {
        const val GRID_VIEW_NUMBER_OF_COLUMNS = 2
    }

    private val viewModel: SearchViewModel by viewModel<SearchViewModel>()

    private val tabLayout by lazy {
        sliding_tabs
    }

    private val searchPager by lazy {
        search_viewpager
    }

    val searchQueryPublisher: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }

    private val searchQueryObservable: Observable<String>
        get() = searchQueryPublisher.hide()

    private val searchAdapter by lazy {
        SearchFragmentPagerAdapter(checkNotNull(this))
    }

    var isMovieSearchScreen: Boolean = true
    private val onTabSelectedListener: TabLayout.OnTabSelectedListener =
        object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    isMovieSearchScreen = it.position == 0
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchPager.adapter = searchAdapter
        TabLayoutMediator(tabLayout, searchPager) { tab, position ->
            if (position == 0) {
                tab.text = activity?.getText(R.string.movies)
            } else {
                tab.text = activity?.getText(R.string.people)
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(onTabSelectedListener)

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

    override fun onDestroy() {
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener)
        super.onDestroy()
    }
}