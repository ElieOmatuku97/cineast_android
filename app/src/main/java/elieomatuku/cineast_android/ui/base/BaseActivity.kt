package elieomatuku.cineast_android.ui.base

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.broadReceiver.NetworkConnectivityBroadcastReceiver
import elieomatuku.cineast_android.business.service.ConnectionService
import elieomatuku.cineast_android.utils.UiUtils
import kotlinx.android.synthetic.main.holder_movie_list.*
import kotlinx.android.synthetic.main.layout_loading.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

abstract class BaseActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by App.kodein.instance()

    private val connectionService: ConnectionService by App.kodein.instance()

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
