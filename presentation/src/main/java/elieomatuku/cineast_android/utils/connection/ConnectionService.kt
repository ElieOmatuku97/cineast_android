package elieomatuku.cineast_android.utils.connection

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import elieomatuku.cineast_android.utils.broadReceiver.ConnectivitySink
import elieomatuku.cineast_android.utils.NetUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ConnectionService @Inject constructor(@ApplicationContext private val appContext: Context) :
    ConnectivitySink {
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
