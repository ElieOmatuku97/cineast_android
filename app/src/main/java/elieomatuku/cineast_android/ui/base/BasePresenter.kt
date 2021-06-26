package elieomatuku.cineast_android.ui.base

import android.os.Bundle
import android.os.Handler
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.service.ConnectionService
import elieomatuku.cineast_android.business.service.ContentService
import io.chthonic.mythos.mvp.Presenter
import io.chthonic.mythos.mvp.Vu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<V> : Presenter<V>(), CoroutineScope where V : Vu {

    private val job: Job by lazy { SupervisorJob() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    protected lateinit var handler: Handler

    protected val rxSubs: io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    protected val connectionService: ConnectionService by App.kodein.instance()

    protected val contentService: ContentService by App.kodein.instance()

    override fun onLink(vu: V, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)
        handler = Handler()
    }

    override fun onUnlink() {
        coroutineContext.cancelChildren()
        rxSubs.clear()
        super.onUnlink()
    }
}
