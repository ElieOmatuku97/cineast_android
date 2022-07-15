package elieomatuku.cineast_android.home

import android.os.Bundle
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.base.BaseActivity
import elieomatuku.cineast_android.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()
    }

    private fun initView() {
        binding.bottomNavig.setOnItemSelectedListener {
            true
        }
    }
}
