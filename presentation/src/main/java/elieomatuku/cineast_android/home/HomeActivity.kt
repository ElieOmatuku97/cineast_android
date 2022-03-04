package elieomatuku.cineast_android.home

import android.os.Bundle
import android.view.Menu
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.base.BaseActivity
import elieomatuku.cineast_android.utils.UiUtils
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    private val bottomNav: BottomNavigationView by lazy {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    private fun initView() {
        bottomNav.setOnNavigationItemSelectedListener {
            toolbar?.title = it.title
            true
        }
    }
}
