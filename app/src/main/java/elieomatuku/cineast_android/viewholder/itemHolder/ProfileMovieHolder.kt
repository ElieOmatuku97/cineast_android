package elieomatuku.cineast_android.viewholder.itemHolder


import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.model.data.Genre
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_profile_movie.view.*
import android.text.Html
import android.text.util.Linkify
import android.text.SpannableString
import android.text.method.LinkMovementMethod
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

    fun update(movie: Movie?, genres: List<Genre>?, homepage: String?) {
        val imageUrl: String? = if (movie?.poster_path != null) {
            UiUtils.getImageUrl(movie.poster_path, itemView.context.getString(R.string.image_small))
        } else null

        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .into(movieProfileImageView)
        }

        movieProfileImageView.setOnClickListener {
            if (movie?.id != null)
                onProfileClickedPicturePublisher.onNext(movie.id)
        }

        itemView.item_title_view.text = movie?.title
        itemView.item_release_view.text = movie?.release_date

        if (movie?.vote_average != null)
            itemView.star_view.rating = movie.vote_average

        val names = if (movie?.genre_ids != null && genres != null) {
            UiUtils.mapMovieGenreIdsWithGenreNames(movie.genre_ids, genres)
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