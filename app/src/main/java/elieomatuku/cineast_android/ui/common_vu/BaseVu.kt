package elieomatuku.cineast_android.ui.common_vu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import elieomatuku.cineast_android.ui.common_activity.BaseActivity
import elieomatuku.cineast_android.ui.details.movie.MovieActivity
import elieomatuku.cineast_android.ui.details.people.PeopleActivity
import elieomatuku.cineast_android.utils.UiUtils
import io.chthonic.mythos.mvp.FragmentWrapper
import io.chthonic.mythos.mvp.Vu
import kotlinx.android.synthetic.main.layout_loading.view.*



abstract class BaseVu (layoutInflater: LayoutInflater, activity: Activity, fragmentWrapper: FragmentWrapper?, parentView: ViewGroup?) :
        Vu (layoutInflater, activity, fragmentWrapper, parentView) {

    val baseActivity: BaseActivity
            get() = activity as BaseActivity

    private val loadingViewDim: Int by lazy {
        activity.resources.getDimensionPixelSize(UiUtils.loadingViewDimRes)
    }


    private val loadingIndicator: PopupWindow by lazy {
        UiUtils.createLoadingIndicator(activity)
    }

    private fun updateLoading(modal: Boolean): Boolean {
        var change = false

        var width = loadingViewDim
        var height = loadingViewDim
        var bgVis = View.GONE

        if (modal) {
            val metrics = UiUtils.getDisplayMetrics(activity)
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


    fun showLoading(modal: Boolean = false) {
        rootView.post{

            // do NOT display loading if fragment is resumed but not visible, e.g. in a viewpager
            val supportFragment = fragmentWrapper?.support
            if ((supportFragment != null) && !supportFragment.userVisibleHint) {
                return@post
            }

            try {
                if (loadingIndicator.isShowing && updateLoading(modal)) {
                    hideLoading()
                }

                if (!loadingIndicator.isShowing) {
                    loadingIndicator.showAtLocation(rootView, Gravity.CENTER, 0, 0)
                }

            } catch (t: Throwable) {

            }
        }
    }


    fun hideLoading() {
        rootView.post {
            try {
                if (loadingIndicator.isShowing) {
                    loadingIndicator.dismiss()
                }

            } catch (t: Throwable) {

            }
        }
    }


    fun guaranteeUiIsActive(): Boolean {

        // note using isDetached since more reliable than isAdded
        return !(destroyed || (fragmentWrapper?.support?.isDetached() ?: false)) && (fragmentWrapper?.support?.isAdded() ?: true)
    }


    fun gotoMovie(params: Bundle){
        val intent = Intent (activity, MovieActivity::class.java)
        intent.putExtras(params)
        activity.startActivity(intent)
    }

    fun gotoPeople(params: Bundle) {
        val intent = Intent (activity, PeopleActivity::class.java)
        intent.putExtras(params)
        activity.startActivity(intent)
    }
}