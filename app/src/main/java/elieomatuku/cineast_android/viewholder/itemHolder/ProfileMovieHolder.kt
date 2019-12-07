package elieomatuku.cineast_android.viewholder.itemHolder


import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_profile_movie.view.*
import android.text.Html
import android.text.util.Linkify
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.widget.RatingBar
import elieomatuku.cineast_android.model.data.MovieSummary
import elieomatuku.cineast_android.fragment.RateDialogFragment



class ProfileMovieHolder(itemView: View, private val onProfileClickedPicturePublisher: PublishSubject<Int>) : ProfileHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_profile_movie, parent, false)
        }

        fun newInstance(parent: ViewGroup, onProfileClickedPicturePublisher: PublishSubject<Int>): ProfileMovieHolder {
            return ProfileMovieHolder(createView(parent), onProfileClickedPicturePublisher)
        }
    }


    private val genresView: TextView by lazy {
        itemView.item_genre_view
    }

    private val movieProfileImageView: AppCompatImageView by lazy {
        itemView.profile_image
    }

    private val linkTextView: TextView by lazy {
        itemView.item_link_view
    }

    private val rateBtn: TextView by lazy {
        itemView.rate_btn
    }

    private val titleView: TextView by lazy {
        itemView.item_title_view
    }

    private val releaseDateView: TextView by lazy {
        itemView.item_release_view
    }

    private val ratingBarView: RatingBar by lazy {
        itemView.movie_rating_bar
    }

    fun update(movieSummary: MovieSummary) {
        val movie = movieSummary.movie
        val imageUrl: String? = if (movie?.poster_path != null) {
            UiUtils.getImageUrl(movie.poster_path, itemView.context.getString(R.string.image_small))
        } else null

        if (!imageUrl.isNullOrEmpty()) {
            movieProfileImageView.visibility = View.VISIBLE
            Picasso.get()
                    .load(imageUrl)
                    .into(movieProfileImageView)
        } else {
            movieProfileImageView.visibility = View.GONE
        }

        movieProfileImageView.setOnClickListener {
            if (movie?.id != null)
                onProfileClickedPicturePublisher.onNext(movie.id)
        }

        val title = movie?.title
        if (title != null) {
            titleView.visibility = View.VISIBLE
            titleView.text = title
        } else {
            titleView.visibility = View.GONE
        }

        val releaseDate = movie?.release_date
        if (releaseDate != null) {
            releaseDateView.visibility = View.VISIBLE
            releaseDateView.text = releaseDate
        } else {
            releaseDateView.visibility = View.GONE
        }

        val voteAverage = movie?.vote_average
        if (voteAverage != null) {
            ratingBarView.visibility = View.VISIBLE
            ratingBarView.rating = voteAverage
        } else {
            ratingBarView.visibility = View.GONE
        }

        val genres = movieSummary.genres
        val genresIds = movie?.genre_ids
        val names = if (genresIds != null && genres != null) {
            UiUtils.mapMovieGenreIdsWithGenreNames(genresIds, genres)
        } else {
            null
        }

        if (!names.isNullOrEmpty()) {
            genresView.visibility = View.VISIBLE
            genresView.text = names
        } else {
            val _genres = movie?.genres
            if (_genres != null) {
                genresView.visibility = View.VISIBLE
                genresView.text = UiUtils.retrieveNameFromGenre(_genres)
            } else {
                genresView.visibility = View.GONE
            }
        }

        val homepage = movieSummary.details?.homepage
        if (!homepage.isNullOrEmpty()) {
            linkTextView.visibility = View.VISIBLE
            val spannable = SpannableString(Html.fromHtml(homepage))
            Linkify.addLinks(spannable, Linkify.WEB_URLS)
            linkTextView.setMovementMethod(LinkMovementMethod.getInstance())
            linkTextView.setText(linkify(spannable), TextView.BufferType.SPANNABLE)

        } else {
            linkTextView.visibility = View.GONE
        }

        rateBtn.setOnClickListener {
            val rateDialogFragment = RateDialogFragment.newInstance(movie)

            if (itemView.context is AppCompatActivity) {
                rateDialogFragment.show((itemView.context as AppCompatActivity).supportFragmentManager, RateDialogFragment.TAG)
            }
        }
    }
}