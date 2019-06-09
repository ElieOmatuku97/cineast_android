package elieomatuku.restapipractice.activity



import android.os.Bundle
import elieomatuku.restapipractice.R
import elieomatuku.restapipractice.presenter.MainPresenter
import elieomatuku.restapipractice.utils.UiUtils
import elieomatuku.restapipractice.vu.MainVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.PresenterCacheLoaderCallback
import timber.log.Timber


class MainActivity: ToolbarMVPActivity<MainPresenter, MainVu>(){
    companion object {
        private val MVP_UID by lazy {
            MainActivity.hashCode()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<MainPresenter, MainVu> {
        return MVPDispatcher(MVP_UID,
                PresenterCacheLoaderCallback(this, { MainPresenter() }),
                ::MainVu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)

        Timber.d("MainActivity onCreate called.")

        mvpDispatcher.vu?.toolbar?.let {
            UiUtils.initToolbar(this, it, false)
        }
        mvpDispatcher.vu?.toolbar?.title = this.getString(R.string.nav_title_discover)
    }
}
