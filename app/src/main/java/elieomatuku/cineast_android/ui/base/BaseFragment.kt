package elieomatuku.cineast_android.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import elieomatuku.cineast_android.business.service.ConnectionService
import elieomatuku.cineast_android.extensions.lifecycleAwareLazy
import elieomatuku.cineast_android.extensions.getSharedViewModel
import elieomatuku.cineast_android.extensions.getViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein
import timber.log.Timber

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

    override fun onResume() {
        super.onResume()

        rxSubs.add(
            connectionService.connectionChangedObserver
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { hasConnection ->

                        if (hasConnection) {
//                                        vu.showLoading()
                        }
                        Timber.d("connectionChangedObserver: hasConnection = $hasConnection, hasEmptyState = ")
                    },
                    { t: Throwable ->

                        Timber.e(t, "Connection Change Observer failed")
                    }
                )
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        rxSubs.clear()
    }
}
