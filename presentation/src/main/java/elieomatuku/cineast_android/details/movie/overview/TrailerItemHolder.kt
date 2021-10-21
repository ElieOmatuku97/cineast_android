package elieomatuku.cineast_android.details.movie.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.fragment.YoutubeFragment
import elieomatuku.cineast_android.utils.UiUtils
import kotlinx.android.synthetic.main.holder_trailer_item.view.*
import timber.log.Timber

class TrailerItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_trailer_item, parent, false)
        }

        fun newInstance(parent: ViewGroup): TrailerItemHolder {
            return TrailerItemHolder(createView(parent))
        }
    }

    private val trailerThumbnailView: ImageView by lazy {
        itemView.trailer_thumbnail
    }

    private val trailerTitleView: TextView by lazy {
        itemView.trailer_title
    }

    fun update(trailerKey: String, trailerName: String?) {
        Timber.d("Path: $trailerKey and name: $trailerName")

        Picasso.get()
            .load(UiUtils.getYoutubeThumbnailPath(trailerKey, "default.jpg"))
            .into(trailerThumbnailView)

        trailerThumbnailView.setOnClickListener {
            val youtubeFragment = YoutubeFragment.newInstance(trailerKey)
            val fm = (itemView.context as AppCompatActivity).supportFragmentManager
            fm.beginTransaction().add(android.R.id.content, youtubeFragment, null).addToBackStack(null).commit()
        }

        trailerName?.let {
            trailerTitleView.visibility = View.VISIBLE
            trailerTitleView.text = it
        }
    }
}
