package elieomatuku.cineast_android.viewholder.itemHolder

import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.utils.UiUtils
import kotlinx.android.synthetic.main.holder_item_movie.view.*



class MovieItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        const val USER_RATING_STRING_FORMAT = "(%.1f, me)"
        const val MOVIE_RATING_STRING_FORMAT = "(%.1f, %d)"

        fun createView(parent: ViewGroup, layoutRes: Int? = null): View{
            return LayoutInflater.from(parent.context).inflate(layoutRes ?: R.layout.holder_item_movie, parent, false)
        }

        fun newInstance(parent: ViewGroup, layoutRes: Int? = null): MovieItemHolder {
            return MovieItemHolder(createView(parent, layoutRes))
        }
    }

    val movieImage: ImageView by lazy {
        itemView.movie_image_view
    }
    private val releaseDate: TextView? by lazy {
        itemView.release_date
    }
    private val movieTitle : TextView? by lazy {
        itemView.movie_name_view
    }

    private val starView: RatingBar? by lazy {
        itemView.findViewById<AppCompatRatingBar?>(R.id.movie_rating_bar)?.also {
            it
        }
    }

    private val voteAverageView: TextView? by lazy {
        itemView.findViewById<TextView>(R.id.vote_average)?.also {
            it
        }
    }

    private val userRatingBar: RatingBar? by lazy {
        itemView.findViewById<AppCompatRatingBar>(R.id.user_rating_bar)?.also {
            it
        }
    }

    private val userRatingTextview: TextView? by lazy {
        itemView.findViewById<TextView>(R.id.user_rating)?.also {
            it
        }
    }

    fun update(movie: Movie){
        val posterPath = movie.poster_path

        if ((posterPath != null) && !(posterPath.isNullOrEmpty())) {
            val imageUrl = UiUtils.getImageUrl(posterPath, itemView.context.getString(R.string.image_small))

            Picasso.get()
                    .load(imageUrl)
                    .into(movieImage)


            releaseDate?.text =  movie.release_date
            movieTitle?.text =   movie.title
        } else {
            itemView.visibility = View.GONE
        }


        movie.vote_average?.let {
            starView?.visibility = View.VISIBLE
            starView?.rating = it

            val voteCount = movie.vote_count

            if (voteCount != null) {
                voteAverageView?.visibility = View.VISIBLE
                voteAverageView?.text = String.format(MOVIE_RATING_STRING_FORMAT, it, voteCount)
            } else {
                voteAverageView?.visibility = View.GONE
            }

        }

        movie.rating?.let {
            userRatingBar?.visibility = View.VISIBLE
            userRatingBar?.rating  = it

            userRatingTextview?.visibility = View.VISIBLE
            userRatingTextview?.text  = String.format(USER_RATING_STRING_FORMAT, it)
        }
    }
}