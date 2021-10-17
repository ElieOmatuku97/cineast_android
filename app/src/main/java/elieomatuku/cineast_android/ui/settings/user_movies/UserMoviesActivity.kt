package elieomatuku.cineast_android.ui.settings.user_movies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.ui.base.BaseActivity
import elieomatuku.cineast_android.ui.contents.ContentsAdapter
import elieomatuku.cineast_android.ui.details.movie.MovieActivity
import elieomatuku.cineast_android.ui.utils.Constants
import elieomatuku.cineast_android.ui.utils.SwipeToDeleteCallback
import elieomatuku.cineast_android.ui.utils.UiUtils
import elieomatuku.cineast_android.ui.utils.consume
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_content.*
import timber.log.Timber
import java.io.Serializable

class UserMoviesActivity : BaseActivity() {
    companion object {
        private const val DISPLAY_FAVORITE_LIST = "favorite_list_key"
        private const val DISPLAY_WATCH_LIST = "watch_list_key"

        const val SCREEN_NAME = "Search"
        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"

        private fun gotoUserPreferencesActivity(context: Context, resources: Int? = null): Intent {
            val intent = Intent(context, UserMoviesActivity::class.java)
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

    private val viewModel: UserMoviesViewModel by viewModel<UserMoviesViewModel>()

    private val movieSelectPublisher: PublishSubject<Content> by lazy {
        PublishSubject.create<Content>()
    }

    private val movieSelectObservable: Observable<Content>
        get() = movieSelectPublisher.hide()

    private val onMovieRemovedPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    private val onMovieRemovedObservable: Observable<Movie>
        get() = onMovieRemovedPublisher.hide()

    private val adapter: ContentsAdapter by lazy {
        UserContentsAdapter(
            movieSelectPublisher,
            R.layout.holder_movie_list,
            onMovieRemovedPublisher
        )
    }

    private val listView: RecyclerView by lazy {
        list_view_container
    }

    private val toolbarView: Toolbar by lazy {
        toolbar
    }

    private var isWatchList: Boolean = false
    private var isFavoriteList: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        isFavoriteList = intent.getBooleanExtra(DISPLAY_FAVORITE_LIST, false)
        isWatchList = intent.getBooleanExtra(DISPLAY_WATCH_LIST, false)

        initData()

        UiUtils.initToolbar(this, toolbarView, true)

        viewModel.viewState.observe(this) { state ->
            if (state.userMovies.isNotEmpty()) {
                updateView(state.userMovies)
            }

            state.viewError?.consume {
                updateErrorView(it.message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        rxSubs.add(
            onMovieRemovedObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        removeMovie(it)
                    },
                    { t: Throwable ->
                        Timber.e("onMovieRemovedObservable failed $t")
                    }
                )
        )

        rxSubs.add(
            movieSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { movie: Content ->
                        val params = Bundle()
                        params.putString(Constants.SCREEN_NAME_KEY, SCREEN_NAME)
                        params.putSerializable(MOVIE_KEY, movie)
                        params.putSerializable(
                            MOVIE_GENRES_KEY,
                            viewModel.genres as Serializable
                        )
                        gotoMovie(params)
                    },
                    { t: Throwable ->
                        Timber.d("movieSelectObservable failed:$t")
                    }
                )
        )
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

    private fun removeMovie(movie: Movie) {
        if (isWatchList) {
            viewModel.removeMovieFromWatchList(movie)
        }

        if (isFavoriteList) {
            viewModel.removeMovieFromFavorites(movie)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Timber.d("onOptionsItemSelected")

        when (item?.itemId) {
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

    private fun updateView(contents: List<Content>?) {
        val screenNameRes = intent.getIntExtra(Constants.SCREEN_NAME_KEY, 0)
        setToolbarTitle(screenNameRes)
        contents?.let {
            if (it.isNotEmpty()) {
                setUpListView(it)
            }
        }
    }

    private fun setToolbarTitle(screenNameRes: Int? = null) {
        toolbarView.title = screenNameRes?.let {
            resources.getString(it)
        } ?: resources.getString(R.string.nav_title_discover)
    }

    private fun setUpListView(contents: List<Content>) {
        adapter.contents = contents.toMutableList()
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter as UserContentsAdapter))
        itemTouchHelper.attachToRecyclerView(listView)
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        listView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }

    private fun gotoMovie(params: Bundle) {
        val intent = Intent(this, MovieActivity::class.java)
        intent.putExtras(params)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        rxSubs.clear()
    }
}
