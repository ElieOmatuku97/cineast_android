package elieomatuku.cineast_android.presentation.home

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.databinding.ActivityHomeBinding
import elieomatuku.cineast_android.presentation.base.BaseActivity

private const val SHOW_APP_BAR_ARG = "showAppBar"
private const val SHOW_BOTTOM_NAV_ARG = "showBottomNav"

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_container) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBar()
        setupBottomNavMenu()
        setDestinationChangedListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun setupBottomNavMenu() {
        binding.bottomNavig.setupWithNavController(navController)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.discover, R.id.search, R.id.settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setDestinationChangedListener() {
        navController.addOnDestinationChangedListener { _, _, arguments ->
            val showAppBar = arguments?.getBoolean(SHOW_APP_BAR_ARG, true) ?: true
            val showBottomNav = arguments?.getBoolean(SHOW_BOTTOM_NAV_ARG, true) ?: true

            if (showAppBar) {
                supportActionBar?.show()
            } else {
                supportActionBar?.hide()
            }
            binding.bottomNavig.isVisible = showBottomNav
        }
    }
}
