package elieomatuku.restapipractice.vu

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.restapipractice.R
import io.chthonic.mythos.mvp.FragmentWrapper


class MyTMBDVu (inflater: LayoutInflater,
                activity: Activity,
                fragmentWrapper: FragmentWrapper?,
                parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView){
    override fun getRootViewLayoutId(): Int {
        return R.layout.vu_my_tmbd
    }
}