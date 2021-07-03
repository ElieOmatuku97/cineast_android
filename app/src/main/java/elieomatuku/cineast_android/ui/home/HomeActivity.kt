package elieomatuku.cineast_android.ui.home

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.ui.base.BaseActivity
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    val sessionPublisher: PublishSubject<Pair<String, Account>> by lazy {
        PublishSubject.create<Pair<String, Account>>()
    }

    private val adapter by lazy {
        HomeFragmentPagerAdapter(supportFragmentManager)
    }

    private val pager by lazy {
        home_pager
    }

    private val navIds: List<Int> by lazy {
        listOf(R.id.action_discover, R.id.action_search, R.id.action_my_TMDb)
    }

    val bottomNav: BottomNavigationView by lazy {
        bottom_navig
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar?.let {
            UiUtils.initToolbar(this, it, false)
        }
        toolbar?.title = this.getString(R.string.nav_title_discover)

        initView()
    }

    private fun initView() {
        pager.adapter = adapter

        toolbar?.let {
            UiUtils.initToolbar(this, it, false)
        }

        bottomNav.setOnNavigationItemSelectedListener {
            toolbar?.title = it.title
            pager.setCurrentItem(navIds.indexOf(it.itemId), true)
            true
        }

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                toolbar?.title = bottomNav.menu.getItem(position).title
                bottomNav.menu.getItem(position).isChecked = true
            }
        })
    }
}
