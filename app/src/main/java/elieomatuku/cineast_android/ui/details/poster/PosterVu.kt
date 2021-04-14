package elieomatuku.cineast_android.ui.details.poster

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.ui.common_vu.BaseVu
import io.chthonic.mythos.mvp.FragmentWrapper
import kotlinx.android.synthetic.main.fragment_poster.view.*

class PosterVu(inflater: LayoutInflater,
               activity: Activity,
               fragmentWrapper: FragmentWrapper?,
               parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    private val posterView: ImageView by lazy {
        rootView.poster_view
    }

    override fun getRootViewLayoutId(): Int {
        return R.layout.fragment_poster
    }


    fun updateImage(moviePosterPath: String?) {
        if (!moviePosterPath.isNullOrEmpty()) {
            val imageUrl = UiUtils.getImageUrl(moviePosterPath, activity.getString(R.string.image_header))
            posterView.visibility = View.VISIBLE
            Picasso.get()
                    .load(imageUrl)
                    .into(posterView)
        }
    }
}