package elieomatuku.cineast_android.viewholder.itemHolder


import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.utils.UiUtils
import kotlinx.android.synthetic.main.holder_header_item.view.*


class HeaderItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup, layoutRes: Int? = null): View {
            return LayoutInflater.from(parent.context).inflate(layoutRes?: R.layout.holder_header_item, parent, false)
        }

        fun newInstance(parent: ViewGroup,layoutRes: Int? = null): HeaderItemHolder {
            return HeaderItemHolder(createView(parent, layoutRes))
        }
    }

    val imageView: ImageView by lazy {
         val view = itemView.movie_image_header
        view
    }


    fun update(movie: Movie){
        val backdropPath = movie.backdrop_path

        if (!backdropPath.isNullOrEmpty()){
            val imageUrl = UiUtils.getImageUrl(backdropPath, itemView.context.getString(R.string.image_header))
            imageView.visibility = View.VISIBLE
            Picasso.get()
                    .load(imageUrl)
                    .into(imageView)
        } else {
            itemView.visibility = View.GONE
        }
    }
}