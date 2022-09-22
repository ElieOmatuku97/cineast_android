package elieomatuku.cineast_android.details.movie

import android.text.Html
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.viewholder.ProfileHolder
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_profile_movie.view.*
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class MovieProfileHolder(
    val composeView: ComposeView,
    private val onProfileClickedPublisher: PublishSubject<Int>,
    onProfileLinkClickedPublisher: PublishSubject<String>
) : ProfileHolder(composeView, onProfileLinkClickedPublisher) {
    companion object {
        private fun createComposeView(parent: ViewGroup): ComposeView {
            return ComposeView(parent.context)
        }

        fun newInstance(
            parent: ViewGroup,
            onProfileClickedPicturePublisher: PublishSubject<Int>,
            onProfileLinkClickedPublisher: PublishSubject<String>
        ): MovieProfileHolder {
            return MovieProfileHolder(
                createComposeView(parent),
                onProfileClickedPicturePublisher,
                onProfileLinkClickedPublisher
            )
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
        composeView.setContent {
            AppCompatTheme {
                MovieProfile(
                    movieSummary = movieSummary
                )
            }
        }
//        val movie = movieSummary.movie
//        val imageUrl: String? = if (movie?.posterPath != null) {
//            UiUtils.getImageUrl(movie.posterPath, itemView.context.getString(R.string.image_small))
//        } else null
//
//        if (!imageUrl.isNullOrEmpty()) {
//            movieProfileImageView.visibility = View.VISIBLE
//            Picasso.get()
//                .load(imageUrl)
//                .into(movieProfileImageView)
//        } else {
//            movieProfileImageView.visibility = View.GONE
//        }
//
//        movieProfileImageView.setOnClickListener {
//            if (movie?.id != null)
//                onProfileClickedPublisher.onNext(movie.id)
//        }
//
//        val title = movie?.title
//        if (title != null) {
//            titleView.visibility = View.VISIBLE
//            titleView.text = title
//        } else {
//            titleView.visibility = View.GONE
//        }
//
//        val releaseDate = movie?.releaseDate
//        if (releaseDate != null) {
//            releaseDateView.visibility = View.VISIBLE
//            releaseDateView.text = releaseDate
//        } else {
//            releaseDateView.visibility = View.GONE
//        }
//
//        val voteAverage = movie?.voteAverage
//        if (voteAverage != null) {
//            ratingBarView.visibility = View.VISIBLE
//            ratingBarView.rating = voteAverage
//        } else {
//            ratingBarView.visibility = View.GONE
//        }
//
//        val genres = movieSummary.genres
//        val genresIds = movie?.genreIds
//        val names = if (genresIds != null && genres != null) {
//            UiUtils.mapMovieGenreIdsWithGenreNames(genresIds, genres)
//        } else {
//            null
//        }
//
//        if (!names.isNullOrEmpty()) {
//            genresView.visibility = View.VISIBLE
//            genresView.text = names
//        } else {
//            genresView.isVisible = movie?.genres != null
//            movie?.genres?.apply {
//                genresView.text = UiUtils.retrieveNameFromGenre(this)
//            }
//        }
//
//        val homepage = movieSummary.facts?.homepage
//        if (!homepage.isNullOrEmpty()) {
//            linkTextView.visibility = View.VISIBLE
//            val spannable = SpannableString(Html.fromHtml(homepage))
//            Linkify.addLinks(spannable, Linkify.WEB_URLS)
//            linkTextView.movementMethod = LinkMovementMethod.getInstance()
//            linkTextView.setText(linkify(spannable), TextView.BufferType.SPANNABLE)
//        } else {
//            linkTextView.visibility = View.GONE
//        }
//
//        if (movieSummary.isEmpty()) {
//            rateBtn.visibility = View.GONE
//        } else {
//            rateBtn.visibility = View.VISIBLE
//        }
//
//        rateBtn.setOnClickListener {
//            val rateDialogFragment = RateDialogFragment.newInstance(movie)
//
//            if (itemView.context is AppCompatActivity) {
//                rateDialogFragment.show(
//                    (itemView.context as AppCompatActivity).supportFragmentManager,
//                    RateDialogFragment.TAG
//                )
//            }
//        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MovieProfile(movieSummary: MovieSummary) {
    val movie = movieSummary.movie
    val imageUrl = UiUtils.getImageUrl(movie?.posterPath, stringResource(id = R.string.image_small))
    val genres = movieSummary.genres
    val genresIds = movie?.genreIds
    val genresNames = if (genresIds != null && genres != null) {
        UiUtils.mapMovieGenreIdsWithGenreNames(genresIds, genres)
    } else {
        movie?.genres?.run {
            UiUtils.retrieveNameFromGenre(this)
        }
    }

    Row {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
            ),
            contentDescription = null,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.movie_summary_image_size))
                .width(dimensionResource(id = R.dimen.movie_summary_image_width))
                .padding(
                    top = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                    start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin)
                )
        )
        Column(
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                start = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right)
            )
        ) {
            movie?.title?.let {
                Text(
                    it,
                    color = colorResource(id = R.color.color_white),
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right)
                    )
                )
            }

            movie?.releaseDate?.let {
                Text(
                    text = it,
                    color = colorResource(id = R.color.color_grey),
                    fontSize = dimensionResource(id = R.dimen.holder_item_movie_textview_size).value.sp,
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.activity_margin_top),
                        start = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right),
                    )
                )
            }

            Text(
                text = "link",
                color = colorResource(id = R.color.color_orange_app),
                fontSize = dimensionResource(id = R.dimen.holder_item_movie_textview_size).value.sp,
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.activity_margin_top),
                    start = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right),
                )
            )

            genresNames?.let {
                Text(
                    text = it,
                    color = colorResource(id = R.color.color_grey),
                    fontSize = dimensionResource(id = R.dimen.holder_item_movie_textview_size).value.sp,
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.activity_margin_top),
                        start = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right),
                    )
                )
            }

            Row(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.activity_margin_top),
                    start = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right),
                )
            ) {
                movie?.voteAverage?.let { voteAverage ->
                    AndroidView(factory = {
                        MaterialRatingBar(it).apply {
                            val params = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            params.height = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                10f,
                                resources.displayMetrics
                            ).toInt()
                            layoutParams = params

                            numStars = 10
                            stepSize = 0.1f
                            rating = voteAverage
                        }
                    })
                }

                if (!movieSummary.isEmpty()) {
                    TextButton(
                        onClick = { },
                        modifier = Modifier.padding(
                            end = dimensionResource(id = R.dimen.holder_profile_movie_rate_margin_right),
                        )
                    ) {
                        Text(
                            stringResource(id = R.string.rate),
                            fontSize = dimensionResource(id = R.dimen.holder_movie_facts_text_size).value.sp,
                            color = colorResource(id = R.color.color_orange_app)
                        )
                    }
                }
            }
        }
    }
}
