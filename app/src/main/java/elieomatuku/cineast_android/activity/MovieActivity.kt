package elieomatuku.cineast_android.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.Menu
import android.view.MenuItem
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.service.UserService
import elieomatuku.cineast_android.presenter.MoviePresenter
import elieomatuku.cineast_android.utils.MovieUtils
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.vu.MovieVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.PresenterCacheLoaderCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import org.kodein.di.generic.instance
import timber.log.Timber

class MovieActivity: ToolbarMVPActivity <MoviePresenter, MovieVu>(){
    companion object {
        private val MVP_UID by lazy {
            MovieActivity.hashCode()
        }
    }

    private var currentMovie: Movie? = null
    private var isInWatchList: Boolean = false
    private val userService : UserService by App.kodein.instance()

    val moviePresentedPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    val watchListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
    }

    private val rxSubs : io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    override fun createMVPDispatcher(): MVPDispatcher<MoviePresenter, MovieVu> {
        return MVPDispatcher(MVP_UID,
                PresenterCacheLoaderCallback(this, { MoviePresenter() }),
                ::MovieVu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mvpDispatcher.vu?.toolbar?.let {
            UiUtils.initToolbar(this, it, true)
        }
    }

    override fun onResume() {
        rxSubs.add(moviePresentedPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe({event: Movie ->
                    onPresenterPublishedNext(event, this)

                }, {t: Throwable ->
                    Timber.e( "moviePresentedPublisher failed")
                }))

        rxSubs.add(watchListCheckPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe( {event: Boolean ->
                    onWatchListCheckPublishedNext(event, this)

                }, {t: Throwable ->
                    Timber.e( "watchListCheckPublisher failed")
                }))
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!super.onSupportNavigateUp()) {
            onBackPressed()
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.item_menu, menu)
        if (menu != null) {
            menu.findItem(R.id.action_share)?.let {
                val colorRes = R.color.color_orange_app
                UiUtils.tintMenuItem(it, this, colorRes)
            }
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_share)?.let {
                it.isVisible = MovieUtils.supportsShare(currentMovie?.id)

        }

        menu?.findItem(R.id.action_watchlist)?.let {
                val menuItem = it
                Timber.d("isInWatchList: $isInWatchList")
                currentMovie?.let {
                    menuItem.isChecked = isInWatchList

                }

                updateWatchListIcon(it, this@MovieActivity)
                it.isVisible = userService.isLoggedIn()
        }

        menu?.findItem(R.id.action_favorites)?.let {
                it.isVisible = userService.isLoggedIn()

        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onSupportNavigateUp()

            R.id.action_share -> {
                onShareMenuClicked(this)
            }

            R.id.action_watchlist -> {
                onWatchListMenuClicked(this, item)
            }

            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun onPresenterPublishedNext(event: Movie, activity: Activity) {
        Timber.d("onPresenterPublishedNext: activity = ${event}")
        currentMovie = event
        activity.invalidateOptionsMenu()
    }


    private fun onWatchListCheckPublishedNext(event: Boolean, activity: Activity) {
        Timber.d("onWatchListCheckPublishedNext: activity = ${event}")
        isInWatchList = event
        activity.invalidateOptionsMenu()
    }


    private fun onShareMenuClicked(activity: Activity) {
        val shareIntent: Intent? = UiUtils.getShareIntent(currentMovie?.title, currentMovie?.id)
        // Make sure there is an activity that supports the intent
        if (shareIntent?.resolveActivity(activity.packageManager) != null ){
            activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.share_title)))
        }
    }

    private fun onWatchListMenuClicked(activity: Activity, item: MenuItem) {
        Timber.d("currentMovie: $currentMovie")
        item.isChecked = !item.isChecked
        val checked = item.isChecked
        updateWatchListIcon(item, activity)

        if (checked) {
            currentMovie?.let {
                userService.addMovieToWatchList(it)
            }

        } else {
            currentMovie?.let {
                userService.removeMovieFromWatchList(it)
            }
        }
    }

    private fun updateWatchListIcon(item: MenuItem, context: Context) {
        val colorRes = R.color.color_orange_app
        if (item.isChecked) {
            item.icon = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_nav_watch_list_selected, context.theme)

        } else {
            item.icon = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_nav_watch_list_unselected, context.theme)
            UiUtils.tintMenuItem(item, context, colorRes)
        }
    }
}