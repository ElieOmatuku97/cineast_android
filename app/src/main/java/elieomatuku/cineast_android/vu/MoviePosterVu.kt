package elieomatuku.restapipractice.vu

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import elieomatuku.restapipractice.R
import elieomatuku.restapipractice.utils.UiUtils
import io.chthonic.mythos.mvp.FragmentWrapper
import kotlinx.android.synthetic.main.fragment_image.view.*

class MoviePosterVu (inflater: LayoutInflater,
                     activity: Activity,
                     fragmentWrapper: FragmentWrapper?,
                     parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    val imageView : ImageView by lazy {
        rootView.image_view
    }

    override fun getRootViewLayoutId(): Int {
        return R.layout.fragment_image
    }



    fun updateImage(moviePosterPath: String?) {
        if (!moviePosterPath.isNullOrEmpty()) {
            val imageUrl = UiUtils.getImageUrl(moviePosterPath, activity.getString(R.string.image_header))
            imageView.visibility = View.VISIBLE
            Picasso.get()
                    .load(imageUrl)
                    .into(imageView)
        }
    }
}