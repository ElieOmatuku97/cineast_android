package elieomatuku.cineast_android.base

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import elieomatuku.cineast_android.broadReceiver.NetworkConnectivityBroadcastReceiver
import elieomatuku.cineast_android.connection.ConnectionService
import elieomatuku.cineast_android.extensions.lifecycleAwareLazy
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import elieomatuku.cineast_android.extensions.*

abstract class BaseActivity : AppCompatActivity, KodeinAware {

    constructor() : super()
    constructor(@LayoutRes resId: Int) : super(resId)

    override val kodein: Kodein by closestKodein()
    val viewModelFactory: ViewModelProvider.Factory by instance()

    protected val connectionService: ConnectionService by instance()

    val rxSubs: io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    private val networkBroadcastReceiver: NetworkConnectivityBroadcastReceiver by lazy {
        NetworkConnectivityBroadcastReceiver(connectionService)
    }

    protected inline fun <reified VM : ViewModel> getViewModel(): VM =
        getViewModel(viewModelFactory)

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> {
        return lifecycleAwareLazy(this) { getViewModel<VM>() }
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
