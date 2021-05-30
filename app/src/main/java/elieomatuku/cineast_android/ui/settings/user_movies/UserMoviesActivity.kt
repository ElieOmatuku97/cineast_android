package elieomatuku.cineast_android.ui.settings.user_movies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.ui.adapter.MoviesAdapter
import elieomatuku.cineast_android.ui.details.movie.MovieActivity
import elieomatuku.cineast_android.utils.SwipeToDeleteCallback
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.vu_item_list.*
import timber.log.Timber

class UserMoviesActivity : AppCompatActivity() {
    companion object {
        private const val DISPLAY_FAVORITE_LIST = "favorite_list_key"
        private const val DISPLAY_WATCH_LIST = "watch_list_key"

        const val SCREEN_NAME_KEY = "screen_name"
        const val SCREEN_NAME = "Search"
        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"

        private fun gotoUserPreferencesActivity(context: Context, resources: Int? = null): Intent {
            val intent = Intent(context, UserMoviesActivity::class.java)
            val params = Bundle()

            if (resources != null) {
                params.putInt(UiUtils.SCREEN_NAME_KEY, resources)
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

    private val viewModel: UserMoviesViewModel by viewModels()

    private val movieSelectPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    private val movieSelectObservable: Observable<Movie>
        get() = movieSelectPublisher.hide()

    private val rxSubs: io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    private val onMovieRemovedPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    private val onMovieRemovedObservable: Observable<Movie>
        get() = onMovieRemovedPublisher.hide()

    private val adapter: MoviesAdapter by lazy {
        UserMoviesAdapter(movieSelectPublisher, R.layout.holder_movie_list, onMovieRemovedPublisher)
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
        setContentView(R.layout.vu_item_list)

        isFavoriteList = intent.getBooleanExtra(DISPLAY_FAVORITE_LIST, false)
        isWatchList = intent.getBooleanExtra(DISPLAY_WATCH_LIST, false)

        initData()

        UiUtils.initToolbar(this, toolbarView, true)
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
                    { movie: Movie ->
                        val params = Bundle()
                        params.putString(SCREEN_NAME_KEY, SCREEN_NAME)
                        params.putParcelable(MOVIE_KEY, movie)
                        params.putParcelableArrayList(MOVIE_GENRES_KEY, viewModel.genresLiveData.value as ArrayList<out Parcelable>)
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

        viewModel.userMovies.observe(this, Observer { res -> updateView(res) })
        viewModel.errorMessage.observe(this, Observer { res -> updateErrorView(res) })
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
        val screenNameRes = intent.getIntExtra(SCREEN_NAME_KEY, 0)
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
        adapter.movies = contents as MutableList<Movie>
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter as UserMoviesAdapter))
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
