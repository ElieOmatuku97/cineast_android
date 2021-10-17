package elieomatuku.cineast_android.ui.base

import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import elieomatuku.cineast_android.ui.connection.ConnectionService
import elieomatuku.cineast_android.ui.extensions.lifecycleAwareLazy
import elieomatuku.cineast_android.ui.extensions.getSharedViewModel
import elieomatuku.cineast_android.ui.extensions.getViewModel
import elieomatuku.cineast_android.ui.utils.UiUtils
import kotlinx.android.synthetic.main.layout_loading.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein

/**
 * Created by elieomatuku on 2021-05-05
 */

abstract class BaseFragment : Fragment, KodeinAware {

    constructor()
    constructor(@LayoutRes resId: Int) : super(resId)

    protected val rxSubs: io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    private val connectionService: ConnectionService by instance()

    override val kodein: Kodein by kodein()
    val viewModelFactory: ViewModelProvider.Factory by instance()

    protected inline fun <reified VM : ViewModel> getViewModel(): VM =
        getViewModel(viewModelFactory)

    protected inline fun <reified VM : ViewModel> getSharedViewModel(): VM =
        getSharedViewModel(viewModelFactory)

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> = lifecycleAwareLazy(this) {
        getViewModel<VM>()
    }

    protected inline fun <reified VM : ViewModel> sharedViewModel(): Lazy<VM> =
        lifecycleAwareLazy(this) {
            getSharedViewModel<VM>()
        }

    private val loadingIndicator: PopupWindow by lazy {
        UiUtils.createLoadingIndicator(this.requireActivity())
    }

    private val loadingViewDim: Int by lazy {
        resources.getDimensionPixelSize(UiUtils.loadingViewDimRes)
    }

    override fun onResume() {
        super.onResume()

//        rxSubs.add(
//            connectionService.connectionChangedObserver
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { hasConnection ->
//
//                        if (hasConnection) {
//                            showLoading(requireView())
//                        }
//                        Timber.d("connectionChangedObserver: hasConnection = $hasConnection, hasEmptyState = ")
//                    },
//                    { t: Throwable ->
//
//                        Timber.e(t, "Connection Change Observer failed")
//                    }
//                )
//        )
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
            val metrics = UiUtils.getDisplayMetrics(requireActivity())
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


    override fun onDestroy() {
        super.onDestroy()
        rxSubs.clear()
    }
}
