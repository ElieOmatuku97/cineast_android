package elieomatuku.cineast_android.home

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_container) as NavHostFragment
        setupBottomNavMenu(navHostFragment.navController)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        binding.bottomNavig.setupWithNavController(navController)
    }
}
