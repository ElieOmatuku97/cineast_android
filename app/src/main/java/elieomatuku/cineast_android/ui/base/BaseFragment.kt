package elieomatuku.cineast_android.ui.base

import androidx.fragment.app.Fragment
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.service.ConnectionService
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber

/**
 * Created by elieomatuku on 2021-05-05
 */

abstract class BaseFragment : Fragment() {
    protected val rxSubs: io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    private val connectionService: ConnectionService by App.kodein.instance()

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
