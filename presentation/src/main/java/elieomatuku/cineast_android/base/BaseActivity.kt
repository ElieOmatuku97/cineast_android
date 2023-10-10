package elieomatuku.cineast_android.base

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import elieomatuku.cineast_android.broadReceiver.NetworkConnectivityBroadcastReceiver
import elieomatuku.cineast_android.connection.ConnectionService
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity {

    constructor() : super()
    constructor(@LayoutRes resId: Int) : super(resId)

    @Inject
    lateinit var connectionService: ConnectionService

    val rxSubs: io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    private val networkBroadcastReceiver: NetworkConnectivityBroadcastReceiver by lazy {
        NetworkConnectivityBroadcastReceiver(connectionService)
    }

    override fun onResume() {
        super.onResume()

        registerReceiver(
            networkBroadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onPause() {
        unregisterReceiver(networkBroadcastReceiver)
        rxSubs.clear()
        super.onPause()
    }
}
