package elieomatuku.cineast_android.ui.common_fragment

import androidx.fragment.app.Fragment


/**
 * Created by elieomatuku on 2021-05-05
 */

abstract class BaseFragment : Fragment() {
    protected val rxSubs: io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }


    override fun onDestroy() {
        super.onDestroy()
        rxSubs.clear()
    }
}