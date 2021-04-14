package elieomatuku.cineast_android.ui.details.movie.movie_team

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Crew
import elieomatuku.cineast_android.utils.UiUtils
import kotlinx.android.synthetic.main.holder_item_people.view.*

class CrewItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_item_people, parent, false)
        }

        fun newInstance(parent: ViewGroup): CrewItemHolder {
            return CrewItemHolder(createView(parent))
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

    fun update(crew: Crew) {
        val profilePath = crew.profile_path

        if (!profilePath.isNullOrEmpty()) {
            peopleImageView.visibility = View.VISIBLE
            val imageUrl = UiUtils.getImageUrl(profilePath, itemView.context.getString(R.string.image_small))
            Picasso.get()
                    .load(imageUrl)
                    .into(peopleImageView)
        } else {
            peopleImageView.visibility = View.GONE
        }

        val crewName = crew.name
        if (!crewName.isNullOrEmpty()) {
            peopleNameView.visibility = View.VISIBLE
            peopleNameView.text = crewName

        } else {
            peopleNameView.visibility = View.GONE
        }

        val crewJob = crew.job
        if (!crewJob.isNullOrEmpty()) {
            peopleJobView.visibility = View.VISIBLE
            peopleJobView.text = crewJob
        } else {
            peopleJobView.visibility = View.GONE
        }
    }
}