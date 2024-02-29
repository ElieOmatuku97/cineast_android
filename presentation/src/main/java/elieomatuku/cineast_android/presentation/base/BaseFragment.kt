package elieomatuku.cineast_android.presentation.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import elieomatuku.cineast_android.presentation.utils.connection.ConnectionService
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-05-05
 */

@AndroidEntryPoint
abstract class BaseFragment : Fragment {

    constructor()
    constructor(@LayoutRes resId: Int) : super(resId)

    protected val rxSubs: io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    @Inject
    lateinit var connectionService: ConnectionService

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

    override fun onDestroyView() {
        super.onDestroyView()
        rxSubs.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        rxSubs.clear()
    }
}
