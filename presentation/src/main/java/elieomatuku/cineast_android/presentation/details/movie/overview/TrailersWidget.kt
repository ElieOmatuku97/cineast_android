package elieomatuku.cineast_android.presentation.details.movie.overview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Trailer
import elieomatuku.cineast_android.presentation.utils.UiUtils

@Composable
fun TrailersWidget(
    trailers: List<Trailer>,
    onItemClick: (trailer: Trailer) -> Unit,
) {
    Surface {
        Column {
            Text(
                text = stringResource(id = R.string.trailers),
                fontSize = dimensionResource(id = R.dimen.text_size_large).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_small)
                )
            )

            LazyRow(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.padding_small),
                    start = dimensionResource(id = R.dimen.padding_small)
                )
            ) {
                items(trailers) { trailer ->
                    TrailersItem(trailer = trailer, onTrailerClick = onItemClick)
                }
            }
        }
    }
}

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
                    top = dimensionResource(id = R.dimen.padding_xsmall),
                    end = dimensionResource(id = R.dimen.padding_medium)
                )
                .height(dimensionResource(id = R.dimen.image_height_xlarge))
                .width(dimensionResource(id = R.dimen.image_width_xlarge))
        )
        trailer.name?.let {
            Text(
                text = it,
                maxLines = 1,
                fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(
                        end = dimensionResource(id = R.dimen.padding_small),
                        bottom = dimensionResource(id = R.dimen.padding_medium)
                    )
                    .widthIn(max = 100.dp)
            )
        }
    }
}
