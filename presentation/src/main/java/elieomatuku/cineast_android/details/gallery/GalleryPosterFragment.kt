package elieomatuku.cineast_android.details.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.utils.UiUtils

class GalleryPosterFragment : BaseFragment() {
    companion object {
        fun newInstance(args: Bundle): GalleryPosterFragment {
            val fragment = GalleryPosterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var posterView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poster, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        posterView = view.findViewById(R.id.poster_view)
        updateView(arguments?.getString(GalleryFragment.MOVIE_POSTER_PATH))
    }

    private fun updateView(moviePosterPath: String?) {
        if (!moviePosterPath.isNullOrEmpty()) {
            val imageUrl =
                UiUtils.getImageUrl(moviePosterPath, activity?.getString(R.string.image_header))
            posterView.visibility = View.VISIBLE
            Picasso.get()
                .load(imageUrl)
                .into(posterView)
        }
    }
}
