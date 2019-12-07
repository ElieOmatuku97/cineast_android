package elieomatuku.cineast_android.viewholder.itemHolder

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.model.data.Person
import elieomatuku.cineast_android.utils.UiUtils
import kotlinx.android.synthetic.main.holder_item_people.view.*

class PopularPeopleItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    companion object {
        fun createView(parent: ViewGroup, layoutRes: Int? = null): View {
            return LayoutInflater.from(parent.context).inflate(layoutRes ?: R.layout.holder_item_people, parent, false)
        }

        fun newInstance(parent: ViewGroup, layoutRes: Int? = null): PopularPeopleItemHolder {
            return PopularPeopleItemHolder(createView(parent, layoutRes))
        }
    }

    val peopleImageView: ImageView? by lazy {
        itemView.people_image_view
    }

    val peopleNameView: TextView? by lazy {
        itemView.people_name_view
    }

    fun update(actor: Person) {
        val profilePath = actor.profile_path
        if (!profilePath.isNullOrEmpty()) {
            peopleImageView?.visibility = View.VISIBLE
            Picasso.get()
                    .load(UiUtils.getImageUrl(profilePath,  itemView.context.getString(R.string.image_small)))
                    .into(peopleImageView)
        } else {
            peopleImageView?.visibility = View.GONE
        }

        val actorName = actor.name
        if (!actorName.isNullOrEmpty()) {
            peopleNameView?.visibility = View.VISIBLE
            peopleNameView?.text = actor.name
        } else {
            peopleNameView?.visibility = View.GONE
        }
    }
}