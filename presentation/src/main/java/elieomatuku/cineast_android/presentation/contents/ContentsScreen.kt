package elieomatuku.cineast_android.presentation.contents

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import elieomatuku.cineast_android.domain.model.Content

@Composable
fun ContentsScreen(
    contents: List<Content>,
    contentItem: @Composable (content: Content) -> Unit = {},
) {
    LazyColumn {
        items(
            items = contents,
            key = { content -> content.id }
        ) { content ->
            contentItem(content)
        }
    }
}