package elieomatuku.cineast_android.details.movie.overview

import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.recyclerview.widget.RecyclerView
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Trailer
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.subjects.PublishSubject

class TrailersHolder(
    val composeView: ComposeView,
    private val onTrailClickedPublisher: PublishSubject<String>
) :
    RecyclerView.ViewHolder(composeView) {
    companion object {
        private fun createComposeView(parent: ViewGroup): ComposeView {
            return ComposeView(parent.context)
        }

        fun newInstance(
            parent: ViewGroup,
            onTrailClickedPublisher: PublishSubject<String>
        ): TrailersHolder {
            return TrailersHolder(createComposeView(parent), onTrailClickedPublisher)
        }
    }

    fun update(movieTrailers: List<Trailer>) {
        if (movieTrailers.isNotEmpty()) {
            composeView.visibility = View.VISIBLE
            composeView.setContent {
                AppCompatTheme {
                    TrailersWidget(trailers = movieTrailers, onItemClick = {
                        it.key?.let {
                            onTrailClickedPublisher.onNext(it)
                        }
                    })
                }
            }
        } else {
            composeView.visibility = View.GONE
        }
    }
}

@Composable
fun TrailersWidget(
    trailers: List<Trailer>,
    onItemClick: (trailer: Trailer) -> Unit,
) {
    Column {
        Text(
            text = stringResource(id = R.string.trailers),
            color = colorResource(R.color.color_white),
            fontSize = dimensionResource(id = R.dimen.toolbar_text_size).value.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin)
            )
        )

        LazyRow(
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.activity_margin_top),
                start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin)
            )
        ) {
            items(trailers) { trailer ->
                TrailersItem(trailer = trailer, onTrailerClick = onItemClick)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun TrailersItem(
    trailer: Trailer,
    onTrailerClick: (trailer: Trailer) -> Unit
) {
    Column(Modifier.clickable(onClick = { onTrailerClick(trailer) })) {
        Image(
            painter = rememberImagePainter(
                data = UiUtils.getYoutubeThumbnailPath(trailer.key, "default.jpg"),
            ),
            contentDescription = null,
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.holder_item_movie_image_view_margin),
                    end = dimensionResource(id = R.dimen.holder_movie_layout_padding_start)
                )
                .height(dimensionResource(id = R.dimen.video_thumbnails_size))
                .width(dimensionResource(id = R.dimen.video_thumbnails_size))
        )
        trailer.name?.let {
            Text(
                text = it,
                color = colorResource(R.color.color_grey_app),
                maxLines = 1,
                fontSize = dimensionResource(id = R.dimen.holder_movie_facts_text_size).value.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(
                        end = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                        bottom = dimensionResource(id = R.dimen.holder_movie_layout_padding_start)
                    )
                    .widthIn(max = 100.dp)
            )
        }
    }
}
