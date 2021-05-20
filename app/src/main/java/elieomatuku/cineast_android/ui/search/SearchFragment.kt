package elieomatuku.cineast_android.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.widget.SearchView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.ui.presenter.PresenterCacheLazy
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment

class SearchFragment : MVPFragment<SearchPresenter, SearchVu>() {
    companion object {
        private val MVP_UID by lazy {
            SearchFragment.hashCode()
        }

        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<SearchPresenter, SearchVu> {
        return MVPDispatcher(
            MVP_UID,
            // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
            PresenterCacheLazy({ SearchPresenter() }),
            ::SearchVu
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = (activity)?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.menu_action_search)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    this@SearchFragment.mvpDispatcher.vu?.searchQueryPublisher?.onNext(query!!)
                    return false
                }
            })
        }

        super.onCreateOptionsMenu(menu, inflater)
    }
}
