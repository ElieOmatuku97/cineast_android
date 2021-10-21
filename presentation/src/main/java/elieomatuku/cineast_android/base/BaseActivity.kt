package elieomatuku.cineast_android.base

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import elieomatuku.cineast_android.broadReceiver.NetworkConnectivityBroadcastReceiver
import elieomatuku.cineast_android.connection.ConnectionService
import elieomatuku.cineast_android.extensions.lifecycleAwareLazy
import elieomatuku.cineast_android.utils.UiUtils
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import elieomatuku.cineast_android.extensions.*
import kotlinx.android.synthetic.main.layout_loading.view.*


abstract class BaseActivity : AppCompatActivity, KodeinAware {

    constructor() : super()
    constructor(@LayoutRes resId: Int) : super(resId)

    override val kodein: Kodein by closestKodein()
    val viewModelFactory: ViewModelProvider.Factory by instance()

    private val connectionService: ConnectionService by instance()

    val rxSubs: io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    private val networkBroadcastReceiver: NetworkConnectivityBroadcastReceiver by lazy {
        NetworkConnectivityBroadcastReceiver(connectionService)
    }

    private val loadingIndicator: PopupWindow by lazy {
        UiUtils.createLoadingIndicator(this)
    }

    private val loadingViewDim: Int by lazy {
        resources.getDimensionPixelSize(UiUtils.loadingViewDimRes)
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

    fun showLoading(view: View, modal: Boolean = false) {
        view.post {
            try {
                if (loadingIndicator.isShowing && updateLoading(modal)) {
                    hideLoading(view)
                }

                if (!loadingIndicator.isShowing) {
                    loadingIndicator.showAtLocation(view, Gravity.CENTER, 0, 0)
                }
            } catch (t: Throwable) {
            }
        }
    }

    fun hideLoading(view: View) {
        view.post {
            try {
                if (loadingIndicator.isShowing) {
                    loadingIndicator.dismiss()
                }
            } catch (t: Throwable) {
            }
        }
    }

    private fun updateLoading(modal: Boolean): Boolean {
        var change = false

        var width = loadingViewDim
        var height = loadingViewDim
        var bgVis = View.GONE

        if (modal) {
            val metrics = UiUtils.getDisplayMetrics(this)
            width = metrics.widthPixels
            height = metrics.heightPixels
            bgVis = View.VISIBLE
        }

        if (loadingIndicator.width != width) {
            loadingIndicator.width = width
            change = true
        }
        if (loadingIndicator.height != height) {
            loadingIndicator.height = height
            change = true
        }
        if (change) {
            loadingIndicator.contentView.loading_bg.visibility = bgVis
        }

        return change
    }
}
