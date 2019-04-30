package elieomatuku.restapipractice.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import elieomatuku.restapipractice.R
import elieomatuku.restapipractice.business.business.model.data.Movie
import elieomatuku.restapipractice.presenter.MoviePresenter
import elieomatuku.restapipractice.utils.MovieUtils
import elieomatuku.restapipractice.utils.UiUtils
import elieomatuku.restapipractice.vu.MovieVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.PresenterCacheLoaderCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class MovieActivity: ToolbarMVPActivity <MoviePresenter, MovieVu>(){
    companion object {
        private val MVP_UID by lazy {
            MovieActivity.hashCode()
        }
    }

    private var currentMovie: Movie? = null

    val moviePresentedPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onSupportNavigateUp()

            R.id.action_share -> {
                onShareMenuClicked(this)
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

    private fun onShareMenuClicked(activity: Activity) {
        val shareIntent: Intent? = UiUtils.getShareIntent(currentMovie?.title, currentMovie?.id)
        // Make sure there is an activity that supports the intent
        if (shareIntent?.resolveActivity(activity.packageManager) != null ){
            activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.share_title)))
        }
    }
}