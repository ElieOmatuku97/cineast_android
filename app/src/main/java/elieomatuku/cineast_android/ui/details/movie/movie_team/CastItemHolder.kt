package elieomatuku.cineast_android.ui.details.movie.movie_team

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Cast
import elieomatuku.cineast_android.utils.UiUtils
import kotlinx.android.synthetic.main.holder_item_people.view.*

class CastItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_item_people, parent, false)
        }

        fun newInstance(parent: ViewGroup): CastItemHolder {
            return CastItemHolder(createView(parent))
        }
    }

    val peopleImageView: ImageView by lazy {
        itemView.people_image_view
    }

    val peopleNameView: TextView by lazy {
        itemView.people_name_view
    }

    val peopleJobView: TextView by lazy {
        itemView.people_job_view
    }

    fun update(cast: Cast) {
        val profilePath = cast.profile_path

        if (!profilePath.isNullOrEmpty()) {
            peopleImageView.visibility = View.VISIBLE
            val imageUrl = UiUtils.getImageUrl(profilePath, itemView.context.getString(R.string.image_small))
            Picasso.get()
                .load(imageUrl)
                .into(peopleImageView)
        } else {
            peopleImageView.visibility = View.GONE
        }

        val castName = cast.name
        if (!castName.isNullOrEmpty()) {
            peopleNameView.visibility = View.VISIBLE
            peopleNameView.text = castName
        } else {
            peopleNameView.visibility = View.GONE
        }

        val castCharacter = cast.character
        if (!castCharacter.isNullOrEmpty()) {
            peopleJobView.visibility = View.VISIBLE
            peopleJobView.text = castCharacter
        } else {
            peopleJobView.visibility = View.GONE
        }
    }
}
