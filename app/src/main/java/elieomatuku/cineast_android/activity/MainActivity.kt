package elieomatuku.cineast_android.activity



import android.os.Bundle
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.presenter.MainPresenter
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.vu.MainVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.PresenterCacheLoaderCallback
import io.reactivex.subjects.PublishSubject
import timber.log.Timber


class MainActivity: ToolbarMVPActivity<MainPresenter, MainVu>(){
    companion object {
        private val MVP_UID by lazy {
           hashCode()
        }
    }

    val rxSubs : io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    val sessionPublisher: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }

    override fun createMVPDispatcher(): MVPDispatcher<MainPresenter, MainVu> {
        return MVPDispatcher(MVP_UID,
                PresenterCacheLoaderCallback(this, { MainPresenter() }),
                ::MainVu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)

        mvpDispatcher.vu?.toolbar?.let {
            UiUtils.initToolbar(this, it, false)
        }
        mvpDispatcher.vu?.toolbar?.title = this.getString(R.string.nav_title_discover)
    }

    override fun onPause() {
        rxSubs.clear()
        super.onPause()
    }

}
