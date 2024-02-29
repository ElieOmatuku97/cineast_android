package elieomatuku.cineast_android.presentation.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.rememberImagePainter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.presentation.utils.UiUtils

/**
 * Created by elieomatuku on 2021-05-30
 */

const val GRID_VIEW_NUMBER_OF_COLUMNS = 2

@Composable
fun ContentGrid(contents: List<Content>, onContentClick: (content: Content) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_VIEW_NUMBER_OF_COLUMNS)
    ) {
        items(contents) { content ->
            ContentGridItem(content = content) {
                onContentClick(it)
            }
        }
    }
}

@Composable
fun ContentGridItem(content: Content, onContentClick: (content: Content) -> Unit) {
    val imagePath = content.imagePath
    if (!imagePath.isNullOrEmpty()) {
        val imageUrl = UiUtils.getImageUrl(imagePath, stringResource(R.string.image_header))
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
            ),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .aspectRatio(1f)
                .clickable(onClick = { onContentClick(content) })
        )
    }
}