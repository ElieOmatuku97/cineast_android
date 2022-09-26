package elieomatuku.cineast_android.details.movie

import android.text.SpannableString
import android.text.style.URLSpan
import android.text.util.Linkify
import android.util.TypedValue
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.utils.UiUtils
import me.zhanghai.android.materialratingbar.MaterialRatingBar


@OptIn(ExperimentalCoilApi::class)
@Composable
fun MovieProfile(
    movieSummary: MovieSummary,
    onProfileClick: (Int) -> Unit,
    onRateClick: () -> Unit,
    gotoLink: (String) -> Unit
) {
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
    val homepage = movieSummary.facts?.homepage

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
                    start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                    bottom = dimensionResource(id = R.dimen.holder_profile_movie_layout_padding_bottom)
                )
                .clickable {
                    movie?.let {
                        onProfileClick(it.id)
                    }
                }
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
                )
            }

            movie?.releaseDate?.let {
                Text(
                    text = it,
                    color = colorResource(id = R.color.color_grey),
                    fontSize = dimensionResource(id = R.dimen.holder_item_movie_textview_size).value.sp,
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.activity_margin_top)
                    )
                )
            }

            homepage?.let {
                val linkStyle = SpanStyle(
                    color = colorResource(id = R.color.color_orange_app),
                    textDecoration = TextDecoration.Underline,
                )
                ClickableText(
                    text = remember(it) { it.linkify(linkStyle) },
                    onClick = { position ->
                        it.linkify(linkStyle).urlAt(position) { link ->
                            gotoLink(link)
                        }
                    },
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.activity_margin_top)
                    )
                )
            }

            genresNames?.let {
                Text(
                    text = it,
                    color = colorResource(id = R.color.color_grey),
                    fontSize = dimensionResource(id = R.dimen.holder_item_movie_textview_size).value.sp,
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.activity_margin_top)
                    )
                )
            }

            Row(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.activity_margin_top)
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                movie?.voteAverage?.let { voteAverage ->
                    AndroidView(factory = {
                        MaterialRatingBar(
                            it,
                            null,
                            R.style.Widget_MaterialRatingBar_RatingBar
                        ).apply {
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
                    Text(
                        stringResource(id = R.string.rate),
                        fontSize = dimensionResource(id = R.dimen.holder_movie_facts_text_size).value.sp,
                        color = colorResource(id = R.color.color_orange_app),
                        modifier = Modifier
                            .padding(end = dimensionResource(id = R.dimen.holder_profile_movie_rate_margin_right))
                            .clickable(onClick = { onRateClick() })
                    )
                }
            }
        }
    }
}

const val URL = "URL"
fun String.linkify(
    linkStyle: SpanStyle,
) = buildAnnotatedString {
    append(this@linkify)

    val spannable = SpannableString(this@linkify)
    Linkify.addLinks(spannable, Linkify.WEB_URLS)

    val spans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
    for (span in spans) {
        val start = spannable.getSpanStart(span)
        val end = spannable.getSpanEnd(span)

        addStyle(
            start = start,
            end = end,
            style = linkStyle,
        )
        addStringAnnotation(
            tag = URL,
            annotation = span.url,
            start = start,
            end = end
        )
    }
}

fun AnnotatedString.urlAt(position: Int, onFound: (String) -> Unit) =
    getStringAnnotations(URL, position, position).firstOrNull()?.item?.let {
        onFound(it)
    }