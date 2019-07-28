package elieomatuku.cineast_android.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.model.data.Widget
import elieomatuku.cineast_android.presenter.ItemListPresenter
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.vu.ItemListVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.PresenterCacheLoaderCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.*

class ItemListActivity: ToolbarMVPActivity <ItemListPresenter, ItemListVu>() {
    companion object {
        private val MVP_UID by lazy {
            ItemListActivity.hashCode()
        }

        const val DISPLAY_FAVORITE_LIST = "favorite_list_key"
        const val DISPLAY_WATCH_LIST = "watch_list_key"

        fun gotoListActivity (context: Context, widgets: List<Widget>, resources: Int? = null, isUserList: Boolean = false): Intent {
            val intent = Intent (context, ItemListActivity::class.java)
            val params = Bundle()
            params.putParcelableArrayList(UiUtils.WIDGET_KEY, widgets as ArrayList<out Parcelable>)

            if (resources != null) {
                params.putInt(UiUtils.SCREEN_NAME_KEY, resources)
            }
            params.putBoolean(UiUtils.USER_LIST_KEY, isUserList)
            intent.putExtras(params)

            return intent
        }

        fun gotoFavoriteList(context: Context, widgets: List<Widget>) {
            val intent = gotoListActivity(context, widgets, R.string.settings_favorites,  true)
            val params = Bundle()
            params.putBoolean(DISPLAY_FAVORITE_LIST, true)
            intent.putExtras(params)

            context.startActivity(intent)
        }

        fun gotoWatchList(context: Context, widgets: List<Widget>) {
            val intent = gotoListActivity(context, widgets, R.string.settings_watchlist,  true)
            val params = Bundle()
            params.putBoolean(DISPLAY_WATCH_LIST, true)
            intent.putExtras(params)

            context.startActivity(intent)
        }

        fun gotoRatedMovies(context: Context, widgets: List<Widget>) {
            val intent = gotoListActivity(context, widgets, R.string.settings_rated,  false)
            context.startActivity(intent)
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
//            R.id.action_edit -> {
//                onEditMenuClicked()
//            }

            else -> super.onOptionsItemSelected(item)
        }

        return true
    }


    private fun onUserListCheckPresenterPublishedNext(event: Boolean) {
        isUserList = event
        invalidateOptionsMenu()
    }

//
//    private fun onEditMenuClicked() {
//
//    }

}