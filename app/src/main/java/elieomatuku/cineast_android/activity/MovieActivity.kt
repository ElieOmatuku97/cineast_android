package elieomatuku.cineast_android.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
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
    private var isInFavoriteList: Boolean = false
    private val userService : UserService by App.kodein.instance()

    val moviePresentedPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    val watchListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
    }

    val favoriteListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
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
                    onPresenterPublishedNext(event)

                }, {t: Throwable ->
                    Timber.e( "moviePresentedPublisher failed")
                }))

        rxSubs.add(watchListCheckPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe( {event: Boolean ->
                    onWatchListCheckPublishedNext(event)

                }, {t: Throwable ->
                    Timber.e( "userListCheckPublisher failed: $t")
                }))

        rxSubs.add(favoriteListCheckPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe( {event: Boolean ->
                    onFavoriteListCheckPublishedNext(event)

                }, {t: Throwable ->
                    Timber.e("favoriteListCheckPublisher failed: $t")
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

                updateWatchListIcon(it)
                it.isVisible = userService.isLoggedIn()
        }

        menu?.findItem(R.id.action_favorites)?.let {
                val menuItem  = it
                Timber.d("isInFavoriteList: $isInFavoriteList")
                currentMovie?.let {
                    menuItem.isChecked = isInFavoriteList
                }
                updateFavoriteListIcon(it)
                it.isVisible = userService.isLoggedIn()
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onSupportNavigateUp()

            R.id.action_share -> {
                onShareMenuClicked()
            }

            R.id.action_watchlist -> {
                onWatchListMenuClicked(item)
            }

            R.id.action_favorites -> {
                onFavoriteListMenuClicked(item)
            }

            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun onPresenterPublishedNext(event: Movie) {
        Timber.d("onPresenterPublishedNext: activity = ${event}")
        currentMovie = event
        invalidateOptionsMenu()
    }


    private fun onWatchListCheckPublishedNext(event: Boolean) {
        Timber.d("onWatchListCheckPublishedNext: activity = ${event}")
        isInWatchList = event
        invalidateOptionsMenu()
    }

    private fun onFavoriteListCheckPublishedNext(event: Boolean) {
        isInFavoriteList = event
        invalidateOptionsMenu()
    }

    private fun onShareMenuClicked() {
        val shareIntent: Intent? = UiUtils.getShareIntent(currentMovie?.title, currentMovie?.id)
        // Make sure there is an activity that supports the intent
        if (shareIntent?.resolveActivity(packageManager) != null ){
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
        }
    }

    private fun onWatchListMenuClicked(item: MenuItem) {
        Timber.d("currentMovie: $currentMovie")
        item.isChecked = !item.isChecked
        val checked = item.isChecked
        updateWatchListIcon(item)

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

    private fun updateWatchListIcon(item: MenuItem) {
        val colorRes = R.color.color_orange_app
        if (item.isChecked) {
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_nav_watch_list_selected, theme)

        } else {
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_nav_watch_list_unselected, theme)
            UiUtils.tintMenuItem(item, this, colorRes)
        }
    }

    private fun onFavoriteListMenuClicked(item: MenuItem) {
        item.isChecked = !item.isChecked
        val checked = item.isChecked
        updateFavoriteListIcon(item)

        if (checked) {
            currentMovie?.let {
                userService.addMovieToFavoriteList(it)
            }

        } else {
            currentMovie?.let {
                userService.removeMovieFromFavoriteList(it)
            }
        }
    }

    private fun updateFavoriteListIcon(item: MenuItem) {
        val colorRes = R.color.color_orange_app
        if (item.isChecked) {
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_star_black_selected, theme)

        } else {
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_star_border_black_unselected, theme)
            UiUtils.tintMenuItem(item, this, colorRes)
        }
    }
}