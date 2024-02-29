package elieomatuku.cineast_android.presentation.settings.usercontents

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import elieomatuku.cineast_android.presentation.R
import elieomatuku.cineast_android.presentation.databinding.ActivityContentBinding
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.presentation.base.BaseActivity
import elieomatuku.cineast_android.presentation.contents.ContentsScreen
import elieomatuku.cineast_android.presentation.contents.SwipeableContentItem
import elieomatuku.cineast_android.presentation.details.movie.MovieFragment
import elieomatuku.cineast_android.presentation.materialtheme.ui.theme.AppTheme
import elieomatuku.cineast_android.presentation.utils.Constants
import elieomatuku.cineast_android.presentation.utils.UiUtils
import elieomatuku.cineast_android.presentation.widgets.EmptyStateWidget
import elieomatuku.cineast_android.presentation.widgets.LoadingIndicatorWidget
import java.io.Serializable

class UserContentsActivity : BaseActivity() {
    companion object {
        private const val DISPLAY_FAVORITE_LIST = "favorite_list_key"
        private const val DISPLAY_WATCH_LIST = "watch_list_key"

        const val SCREEN_NAME = "Search"
        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"

        private fun gotoUserPreferencesActivity(context: Context, resources: Int? = null): Intent {
            val intent = Intent(context, UserContentsActivity::class.java)
            val params = Bundle()

            if (resources != null) {
                params.putInt(Constants.SCREEN_NAME_KEY, resources)
            }

            intent.putExtras(params)

            return intent
        }

        fun gotoFavorites(context: Context) {
            val intent = gotoUserPreferencesActivity(context, R.string.settings_favorites)
            val params = Bundle()
            params.putBoolean(DISPLAY_FAVORITE_LIST, true)
            intent.putExtras(params)

            context.startActivity(intent)
        }

        fun gotoWatchList(context: Context) {
            val intent = gotoUserPreferencesActivity(context, R.string.settings_watchlist)
            val params = Bundle()
            params.putBoolean(DISPLAY_WATCH_LIST, true)
            intent.putExtras(params)

            context.startActivity(intent)
        }

        fun gotoRatedMovies(context: Context) {
            val intent = gotoUserPreferencesActivity(context, R.string.settings_rated)
            context.startActivity(intent)
        }
    }

    private val viewModel: UserContentsViewModel by viewModels()

    private lateinit var binding: ActivityContentBinding

    private var isWatchList: Boolean = false
    private var isFavoriteList: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        isFavoriteList = intent.getBooleanExtra(DISPLAY_FAVORITE_LIST, false)
        isWatchList = intent.getBooleanExtra(DISPLAY_WATCH_LIST, false)

        initData()
        UiUtils.initToolbar(this, binding.toolbar, true)

        updateView()
    }

    private fun initData() {
        when {
            isFavoriteList -> {
                viewModel.getFavourites()
            }
            isWatchList -> {
                viewModel.getWatchList()
            }
            else -> {
                viewModel.getRatedMovies()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)

        return true
    }

//    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onSupportNavigateUp()
//            R.id.action_edit -> {
//                onEditMenuClicked()
//            }

            else -> super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!super.onSupportNavigateUp()) {
            onBackPressed()
        }
        return true
    }

    private fun updateView() {
        val screenNameRes = intent.getIntExtra(Constants.SCREEN_NAME_KEY, 0)
        setToolbarTitle(screenNameRes)
        binding.composeView.setContent {
            AppTheme {
                UserContentScreen(
                    hasNetworkConnection = connectionService.hasNetworkConnection,
                    onContentClick = {
                        if (it is Movie) {
                            gotoMovie(it)
                        }
                    },
                    onSwipeItem = {
                        if (it is Movie) {
                            removeMovie(it)
                        }
                    }
                )
            }
        }
    }

    private fun setToolbarTitle(screenNameRes: Int? = null) {
        binding.toolbar.title = screenNameRes?.let {
            resources.getString(it)
        } ?: resources.getString(R.string.nav_title_discover)
    }

    private fun gotoMovie(movie: Movie) {
        val params = Bundle()
        params.putString(Constants.SCREEN_NAME_KEY, SCREEN_NAME)
        params.putSerializable(MOVIE_KEY, movie)
        params.putSerializable(
            MOVIE_GENRES_KEY,
            viewModel.genres as Serializable
        )
        val intent = Intent(this, MovieFragment::class.java)
        intent.putExtras(params)
        startActivity(intent)
    }

    private fun removeMovie(movie: Movie) {
        if (isWatchList) {
            viewModel.removeMovieFromWatchList(movie)
        }

        if (isFavoriteList) {
            viewModel.removeMovieFromFavorites(movie)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        rxSubs.clear()
    }
}

@Composable
fun UserContentScreen(
    viewModel: UserContentsViewModel = hiltViewModel(),
    hasNetworkConnection: Boolean,
    onContentClick: (content: Content) -> Unit,
    onSwipeItem: ((content: Content) -> Unit)
) {

    val viewState by viewModel.viewState.observeAsState()
    viewState?.apply {
        Box(modifier = Modifier.fillMaxSize()) {
            if (contents.isNotEmpty()) {
                ContentsScreen(
                    contents = contents,
                    contentItem = { content ->
                        SwipeableContentItem(
                            content = content,
                            onContentClick = onContentClick,
                            onSwipeItem = onSwipeItem
                        )
                    }
                )
            }

            viewError?.consume()?.apply {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EmptyStateWidget(
                        errorMsg = message,
                        hasNetworkConnection = hasNetworkConnection
                    )
                }
            }

            if (isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingIndicatorWidget()
                }
            }
        }
    }

}
