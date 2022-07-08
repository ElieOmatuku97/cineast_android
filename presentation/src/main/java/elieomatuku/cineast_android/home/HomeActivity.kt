package elieomatuku.cineast_android.home

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    private val bottomNav: BottomNavigationView by lazy {
        bottom_navig
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
    }

    private fun initView() {
        bottomNav.setOnItemSelectedListener {
            true
        }
    }
}
