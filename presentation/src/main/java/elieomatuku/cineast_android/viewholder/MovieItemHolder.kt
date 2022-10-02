package elieomatuku.cineast_android.viewholder

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.RecyclerView
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.utils.UiUtils

class MovieItemHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView) {
    companion object {
        private fun createComposeView(parent: ViewGroup): ComposeView {
            return ComposeView(parent.context)
        }

        fun newInstance(parent: ViewGroup): MovieItemHolder {
            return MovieItemHolder(createComposeView(parent))
        }
    }

    fun update(movie: Movie) {
        composeView.setContent {
            AppCompatTheme {
                MovieItem(movie = movie)
            }
        }
    }
}

const val USER_RATING_STRING_FORMAT = "(%.1f, me)"
const val MOVIE_RATING_STRING_FORMAT = "(%.1f, %d)"

@Composable
fun MovieItem(movie: Movie) {
    ContentItem(
        imagePath = movie.posterPath,
        title = movie.title ?: movie.originalTitle,
        subTitle = movie.releaseDate
    ) {
        movie.voteAverage?.let { voteAverage ->
            Row(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.padding_small)
                )
            ) {
                Text(
                    text = String.format(
                        MOVIE_RATING_STRING_FORMAT,
                        voteAverage,
                        movie.voteCount
                    ),
                    color = colorResource(id = R.color.color_white),
                    fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                    modifier = Modifier.padding(
                        end = dimensionResource(id = R.dimen.padding_small)
                    )
                )
                AndroidView(factory = {
                    AppCompatRatingBar(
                        it,
                        null,
                        R.attr.ratingBarStyleSmall
                    ).apply {
                        val params = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        rating = 2f
                        scaleX = 0.7f
                        scaleY = 0.65f
                        pivotX = 0f
                        pivotY = 0f
                        layoutParams = params

                        numStars = 10
                        stepSize = 0.1f
                        rating = voteAverage
                    }
                })
            }
        }

        movie.currentUserRating?.let { userRating ->
            Row(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.padding_small)
                ),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Text(
                    text = String.format(USER_RATING_STRING_FORMAT, userRating),
                    color = colorResource(id = R.color.color_white),
                    fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                    modifier = Modifier.padding(
                        end = dimensionResource(id = R.dimen.padding_small)
                    )
                )
                AndroidView(factory = {
                    AppCompatRatingBar(
                        it,
                        null,
                        R.attr.ratingBarStyleSmall
                    ).apply {
                        val params = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        rating = 2f
                        scaleX = 0.7f
                        scaleY = 0.65f
                        pivotX = 0f
                        pivotY = 0f
                        layoutParams = params

                        numStars = 10
                        stepSize = 0.1f
                        rating = userRating
                    }
                })
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ContentItem(
    imagePath: String?,
    title: String?,
    subTitle: String? = null,
    child: @Composable () -> Unit = {}
) {
    val fallBackUrl = stringResource(R.string.image_small)
    val imageUrl = remember {
        UiUtils.getImageUrl(imagePath, fallBackUrl)
    }
    Column(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))) {
        Row {
            Image(
                painter = rememberImagePainter(
                    data = imageUrl,
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.image_height_xxlarge))
                    .width(dimensionResource(id = R.dimen.image_width_xlarge))
                    .padding(
                        top = dimensionResource(id = R.dimen.padding_small),
                        start = dimensionResource(id = R.dimen.padding_small),
                        bottom = dimensionResource(id = R.dimen.padding_medium)
                    )

            )
            Column(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.padding_small),
                    start = dimensionResource(id = R.dimen.padding_small)
                )
            ) {
                title?.let {
                    Text(
                        it,
                        fontSize = dimensionResource(id = R.dimen.text_size_medium).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.color_white),
                    )
                }

                subTitle?.let {
                    Text(
                        text = it,
                        color = colorResource(id = R.color.color_grey),
                        fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                        modifier = Modifier.padding(
                            top = dimensionResource(id = R.dimen.padding_small)
                        )
                    )
                }

                child()

            }
        }
        Divider(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_xlarge)),
            color = colorResource(id = R.color.color_grey_app),
            thickness = 0.5.dp,
            startIndent = dimensionResource(id = R.dimen.padding_xlarge)
        )
    }
}
