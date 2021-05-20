package elieomatuku.cineast_android.ui.details.people

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.ui.activity.ToolbarMVPActivity
import elieomatuku.cineast_android.utils.MovieUtils
import elieomatuku.cineast_android.utils.UiUtils
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.PresenterCacheLoaderCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class PeopleActivity : ToolbarMVPActivity<PeoplePresenter, PeopleVu>() {
    companion object {
        private val MVP_UID by lazy {
            hashCode()
        }
        private val LOG_TAG by lazy {
            PeopleActivity::class.java.simpleName
        }
    }

    private var currentPerson: Person? = null

    val personPresentedPublisher: PublishSubject<Person> by lazy {
        PublishSubject.create<Person>()
    }

    override fun createMVPDispatcher(): MVPDispatcher<PeoplePresenter, PeopleVu> {
        return MVPDispatcher(
            MVP_UID,
            PresenterCacheLoaderCallback(this, { PeoplePresenter() }),
            ::PeopleVu
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mvpDispatcher.vu?.toolbar?.let {
            UiUtils.initToolbar(this, it, false)
        }
    }

    override fun onResume() {
        rxSubs.add(
            personPresentedPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { event: Person ->
                        onPresenterPublishedNext(event, this)
                    },
                    { t: Throwable ->
                        Log.e(LOG_TAG, "personPresentedPublisher failed")
                    }
                )
        )
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
            it.isVisible = MovieUtils.supportsShare(currentPerson?.id)
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

    private fun onPresenterPublishedNext(event: Person, activity: Activity) {
        Log.d(LOG_TAG, "onPresenterPublishedNext: activity = $event")
        currentPerson = event
        activity.invalidateOptionsMenu()
    }

    private fun onShareMenuClicked(activity: Activity) {
        val tmdbPath = "person"
        val shareIntent: Intent? = UiUtils.getShareIntent(currentPerson?.name, currentPerson?.id, tmdbPath)

        // Make sure there is an activity that supports the intent
        if (shareIntent?.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.share_title)))
        }
    }
}
