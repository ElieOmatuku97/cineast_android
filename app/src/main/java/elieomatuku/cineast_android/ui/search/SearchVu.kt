package elieomatuku.cineast_android.ui.search

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.ui.contents.ContentsActivity
import elieomatuku.cineast_android.ui.vu.ToolbarVu
import elieomatuku.cineast_android.utils.UiUtils
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.vu_search.view.*
import timber.log.Timber

class SearchVu(
    inflater: LayoutInflater,
    activity: Activity,
    fragmentWrapper: FragmentWrapper?,
    parentView: ViewGroup?
) : ToolbarVu(
    inflater,
    activity = activity,
    fragmentWrapper = fragmentWrapper,
    parentView = parentView
) {

    companion object {
        const val GRID_VIEW_NUMBER_OF_COLUMNS = 2
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
    private val onTabSelectedListener: TabLayout.OnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            Timber.d("Selected Tab ${tab?.position}")
            tab?.let {
                isMovieSearchScreen = it.position == 0
            }
        }
    }

    override fun onCreate() {
        Timber.d("SearchVu created")

        searchPager.adapter = searchAdapter
        TabLayoutMediator(tabLayout, searchPager) { tab, position ->
            Timber.d("tab at position = $position created")
            if (position == 0) {
                tab.text = activity.getText(R.string.movies)
            } else {
                tab.text = activity.getText(R.string.people)
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(onTabSelectedListener)

        toolbar?.let {
            UiUtils.initToolbar(activity as AppCompatActivity, toolbar)
        }
    }

    fun showSearchResults(results: List<Content>?) {
        results?.let {
            ContentsActivity.startActivity(activity, results, R.string.search_hint)
        }
    }

    override fun onDestroy() {
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener)
        super.onDestroy()
    }
}
