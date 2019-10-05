package elieomatuku.cineast_android.activity

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.model.data.broadReceiver.NetworkConnectivityBroadcastReceiver
import elieomatuku.cineast_android.business.service.ConnectionService
import org.kodein.di.generic.instance


abstract class BaseActivity : AppCompatActivity() {


    protected val connectionService: ConnectionService by App.kodein.instance()

    val rxSubs: io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }


    private val networkBroadcastReceiver: NetworkConnectivityBroadcastReceiver by lazy {
        NetworkConnectivityBroadcastReceiver(connectionService)
    }


    override fun onResume() {
        super.onResume()


        registerReceiver(networkBroadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }


    override fun onPause() {
        unregisterReceiver(networkBroadcastReceiver)
        super.onPause()
    }
}