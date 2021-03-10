package elieomatuku.cineast_android.search


import android.app.SearchManager
import android.content.Context
import elieomatuku.cineast_android.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.vu.SearchVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.view.*
import elieomatuku.cineast_android.R


class SearchFragment: MVPFragment<SearchPresenter, SearchVu>() {
    companion object {
        private val MVP_UID by lazy {
            SearchFragment.hashCode()
        }

        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<SearchPresenter, SearchVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ SearchPresenter() }),
               ::SearchVu)
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

            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
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