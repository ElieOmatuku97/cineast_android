package elieomatuku.cineast_android.presentation.discover

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.rememberImagePainter
import elieomatuku.cineast_android.presentation.R
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.presentation.utils.UiUtils

@Composable
fun HeaderItem(
    movie: Movie,
    onMovieClick: (movie: Movie) -> Unit
) {
    val backdropPath = movie.backdropPath
    val imageUrl = UiUtils.getImageUrl(backdropPath, stringResource(R.string.image_header))
    Image(
        painter = rememberImagePainter(
            data = imageUrl,
        ),
        contentScale = ContentScale.FillBounds,
        contentDescription = null,
        modifier = Modifier
            .aspectRatio(1.0f)
            .clickable(onClick = { onMovieClick.invoke(movie) })
    )

}
