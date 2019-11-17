package elieomatuku.cineast_android.presenter

import android.os.Bundle
import io.chthonic.mythos.mvp.Presenter
import io.chthonic.mythos.mvp.Vu
import android.os.Handler
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.service.ConnectionService
import org.kodein.di.generic.instance


abstract class BasePresenter<V>: Presenter<V>() where V: Vu{

    protected lateinit var handler: Handler

    protected val rxSubs : io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    protected val connectionService: ConnectionService by App.kodein.instance()

    override fun onLink(vu: V, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)
        handler = Handler()
    }

    override fun onUnlink() {
        rxSubs.clear()
        super.onUnlink()
    }
}