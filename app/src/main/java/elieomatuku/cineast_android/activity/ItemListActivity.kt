package elieomatuku.cineast_android.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.presenter.ItemListPresenter
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.vu.ItemListVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.PresenterCacheLoaderCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class ItemListActivity: ToolbarMVPActivity <ItemListPresenter, ItemListVu>() {
    companion object {
        private val MVP_UID by lazy {
            ItemListActivity.hashCode()
        }
    }

    private var isUserList: Boolean = false

    override fun createMVPDispatcher(): MVPDispatcher<ItemListPresenter, ItemListVu> {
        return MVPDispatcher(MVP_UID,
                PresenterCacheLoaderCallback(this, { ItemListPresenter() }),
                ::ItemListVu)
    }

    private val rxSubs : io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    val userListCheckPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mvpDispatcher.vu?.toolbar?.let {
            UiUtils.initToolbar(this, it, true)
        }
    }

    override fun onResume() {
        rxSubs.add(userListCheckPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe({event: Boolean ->
                    onUserListCheckPresenterPublishedNext(event)

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
        menuInflater.inflate(R.menu.edit_menu, menu)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_edit)?.let {
            it.isVisible = isUserList

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Timber.d("onOptionsItemSelected")

        when (item?.itemId) {
            android.R.id.home -> onSupportNavigateUp()
            R.id.action_edit -> {
                onEditMenuClicked()
            }

            else -> super.onOptionsItemSelected(item)
        }

        return true
    }


    private fun onUserListCheckPresenterPublishedNext(event: Boolean) {
        isUserList = event
        invalidateOptionsMenu()
    }


    private fun onEditMenuClicked() {

    }

}