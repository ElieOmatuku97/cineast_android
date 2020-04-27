package elieomatuku.cineast_android.vu

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.cineast_android.adapter.SearchFragmentPagerAdapter
import elieomatuku.cineast_android.utils.UiUtils
import kotlinx.android.synthetic.main.vu_main.view.*
import kotlinx.android.synthetic.main.vu_search.view.*
import com.google.android.material.tabs.TabLayoutMediator
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.activity.ItemListActivity
import elieomatuku.cineast_android.core.model.Content
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber


class SearchVu(inflater: LayoutInflater,
               activity: Activity,
               fragmentWrapper: FragmentWrapper?,
               parentView: ViewGroup?) : ToolbarVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    companion object {
        const val GRIDVIEW_NUMBER_OF_COLUMNS = 2
    }

    override val toolbar: Toolbar?
        get() = rootView.toolbar

    override fun getRootViewLayoutId(): Int {
        return R.layout.vu_search
    }

    val searchQueryPublisher: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }

    val searchQueryObservable: Observable<String>
        get() = searchQueryPublisher.hide()

    private val searchAdapter by lazy {
        SearchFragmentPagerAdapter(checkNotNull(fragmentWrapper?.support))
    }

    private val tabLayout by lazy {
        rootView.sliding_tabs
    }

    private val searchPager by lazy {
        rootView.search_viewpager
    }

    var isMovieSearchScreen: Boolean = true

    override fun onCreate() {
        Timber.d("SearchVu created")

        searchPager.adapter = searchAdapter
        TabLayoutMediator(tabLayout, searchPager) { tab, position ->
            if (position == 0) {
                tab.text = activity.getText(R.string.movies)
                isMovieSearchScreen = true
            } else {
                tab.text = activity.getText(R.string.people)
                isMovieSearchScreen = false
            }
        }.attach()

        if (toolbar != null) {
            UiUtils.initToolbar(activity as AppCompatActivity, toolbar)
        }
    }

    fun showSearchResults(results: List<Content>?) {
        results?.let {
            ItemListActivity.startItemListActivity(activity, results, R.string.search_hint)
        }
    }
}


