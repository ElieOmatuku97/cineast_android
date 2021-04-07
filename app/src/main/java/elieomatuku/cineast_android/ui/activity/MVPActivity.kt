package elieomatuku.cineast_android.ui.activity

import android.os.Bundle
import androidx.loader.app.LoaderManager
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.Presenter
import io.chthonic.mythos.mvp.Vu

abstract class MVPActivity<P, V>: BaseActivity() where P : Presenter<V>, V : Vu {
    private val mvpDispatcher: MVPDispatcher<P, V> by lazy {
        createMVPDispatcher()
    }

    /**
     * @return MVPDispatcher instance used to coordinate MVP pattern.
     */
    protected abstract fun createMVPDispatcher(): MVPDispatcher<P, V>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mvpDispatcher.restorePresenterState(savedInstanceState)
        mvpDispatcher.createVu(this.layoutInflater, this)
        setContentView(mvpDispatcher.vu!!.rootView)

        if (mvpDispatcher.presenterCache is LoaderManager.LoaderCallbacks<*>) {
            supportLoaderManager.initLoader(mvpDispatcher.uid,
                    null,
                    @Suppress("UNCHECKED_CAST") // unable to fully check generics in kotlin
                    mvpDispatcher.presenterCache as LoaderManager.LoaderCallbacks<P>)
        }
    }

    override fun onResume() {
        super.onResume()
        mvpDispatcher.linkPresenter(this.intent.extras)
    }

    override fun onPause() {
        mvpDispatcher.unlinkPresenter()
        super.onPause()
    }

    override fun onDestroy() {
        mvpDispatcher.destroyVu()
        super.onDestroy()
    }


    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        mvpDispatcher.savePresenterState(outState)
    }
}