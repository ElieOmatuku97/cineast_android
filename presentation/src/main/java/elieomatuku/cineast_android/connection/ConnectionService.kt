package elieomatuku.cineast_android.connection

import android.app.Application
import elieomatuku.cineast_android.broadReceiver.ConnectivitySink
import elieomatuku.cineast_android.utils.NetUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ConnectionService(private val appContext: Application) : ConnectivitySink {
    private val connectionChangedPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
    }

    val connectionChangedObserver: Observable<Boolean>
        get() = connectionChangedPublisher.hide()

    val hasNetworkConnection: Boolean
        get() = NetUtils.isOnline(appContext)

    override fun updateNetworkConnected(isConnected: Boolean) {
        connectionChangedPublisher.onNext(isConnected)
    }
}
