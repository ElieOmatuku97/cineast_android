package elieomatuku.cineast_android.contents

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.utils.UiUtils

const val USER_RATING_STRING_FORMAT = "(%.1f, me)"
const val MOVIE_RATING_STRING_FORMAT = "(%.1f, %d)"

@Composable
fun MovieItem(movie: Movie, onContentClick: (content: Content) -> Unit = {}) {
    ContentItem(
        content = movie,
        onContentClick = onContentClick
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
    content: Content,
    onContentClick: (content: Content) -> Unit = {},
    child: @Composable () -> Unit = {},
) {
    val fallBackUrl = stringResource(R.string.image_small)
    val imageUrl = remember {
        UiUtils.getImageUrl(content.imagePath, fallBackUrl)
    }
    Column(
        modifier = Modifier
            .padding(bottom = dimensionResource(id = R.dimen.padding_small))
            .clickable { onContentClick(content) }) {
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
                content.title?.let {
                    Text(
                        it,
                        fontSize = dimensionResource(id = R.dimen.text_size_medium).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.color_white),
                    )
                }

                content.subTitle?.let {
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableContentItem(
    content: Content,
    onContentClick: (content: Content) -> Unit,
    onSwipeItem: (content: Content) -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart) {
                onSwipeItem(content)
            }
            true
        }
    )
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(
            DismissDirection.EndToStart
        ),
        dismissThresholds = {
            FractionalThreshold(0.2f)
        },
        background = {
            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    DismissValue.DismissedToStart -> Color.Red
                    else -> Color.Black
                }
            )

            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = {
            Card(
                elevation = animateDpAsState(
                    if (dismissState.dismissDirection != null) 4.dp else 0.dp
                ).value,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onContentClick(content)
                    },
                backgroundColor = Color.Black
            ) {
                when (content) {
                    is Person -> {
                        ContentItem(content = content)
                    }
                    is Movie -> {
                        MovieItem(movie = content)
                    }
                }
            }
        }
    )
}
