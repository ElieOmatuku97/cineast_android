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
import com.google.android.material.tabs.TabLayout
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.activity.ItemListActivity
import elieomatuku.cineast_android.model.data.Content
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

    private var searchAdapter: SearchFragmentPagerAdapter? = null

    val tabLayout by lazy {
        rootView.sliding_tabs
    }

    private val searchPager by lazy {
        val pager = rootView.search_viewpager

        pager
    }

    val tabLayoutOnPageChangeListener by lazy {
        TabLayout.TabLayoutOnPageChangeListener(tabLayout)
    }

    var isMovieSearchScreen: Boolean = true

    override fun onCreate() {
        Timber.d("SearchVu created")

        searchAdapter = fragmentWrapper?.support?.childFragmentManager?.let {
            SearchFragmentPagerAdapter(it)
        }

        if (searchAdapter != null) {
            searchPager.adapter = searchAdapter
            searchPager.addOnPageChangeListener(tabLayoutOnPageChangeListener)
        }

        if (toolbar != null) {
            UiUtils.initToolbar(activity as AppCompatActivity, toolbar)
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    if (tab.position == 0) {
                        isMovieSearchScreen = true
                    } else {
                        isMovieSearchScreen = false
                    }
                    searchPager.setCurrentItem(tab.position, true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Timber.d("Tab : ${tab?.position} unselected")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Timber.d("Tab : ${tab?.position} reselected")
            }
        })
    }


    fun openItemListActivity(results: List<Content>?) {
        if (results != null) {
            ItemListActivity.startItemListActivity(activity, results, R.string.search_hint)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchPager.removeOnPageChangeListener(tabLayoutOnPageChangeListener)

    }
}


