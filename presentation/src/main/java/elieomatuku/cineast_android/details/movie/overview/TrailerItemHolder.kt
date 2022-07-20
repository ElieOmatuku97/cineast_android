package elieomatuku.cineast_android.details.movie.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_trailer_item.view.*

class TrailerItemHolder(
    itemView: View,
    private val onTrailClickedPublisher: PublishSubject<String>
) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_trailer_item, parent, false)
        }

        fun newInstance(
            parent: ViewGroup,
            onTrailClickedPublisher: PublishSubject<String>
        ): TrailerItemHolder {
            return TrailerItemHolder(createView(parent), onTrailClickedPublisher)
        }
    }

    private val trailerThumbnailView: ImageView by lazy {
        itemView.trailer_thumbnail
    }

    private val trailerTitleView: TextView by lazy {
        itemView.trailer_title
    }

    fun update(trailerKey: String, trailerName: String?) {
        Picasso.get()
            .load(UiUtils.getYoutubeThumbnailPath(trailerKey, "default.jpg"))
            .into(trailerThumbnailView)

        trailerThumbnailView.setOnClickListener {
            onTrailClickedPublisher.onNext(trailerKey)
        }

        trailerName?.let {
            trailerTitleView.visibility = View.VISIBLE
            trailerTitleView.text = it
        }
    }
}
