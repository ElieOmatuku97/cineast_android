package elieomatuku.cineast_android.ui.common_vu

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import io.chthonic.mythos.mvp.FragmentWrapper

abstract class ToolbarVu(layoutInflater: LayoutInflater, activity: Activity, fragmentWrapper: FragmentWrapper?, parentView: ViewGroup?) :
    BaseVu(layoutInflater, activity, fragmentWrapper, parentView) {

    abstract val toolbar: Toolbar?

    fun setToolbarTitle(titleRes: Int) {
        setToolbarTitle(activity.getString(titleRes))
    }

    private fun setToolbarTitle(title: String) {
        if (!guaranteeUiIsActive()) {
            return
        }
        baseActivity.supportActionBar?.title = title
    }
}
