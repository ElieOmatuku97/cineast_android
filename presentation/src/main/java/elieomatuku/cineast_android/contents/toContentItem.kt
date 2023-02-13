package elieomatuku.cineast_android.contents

import androidx.compose.runtime.Composable
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person

@Composable
fun Content.contentItem(onContentClick: (content: Content) -> Unit) {
    when (this) {
        is Person -> {
            ContentItem(
                content = this,
                onContentClick = onContentClick,
            )
        }
        is Movie -> {
            MovieItem(
                movie = this,
                onContentClick = onContentClick
            )
        }
    }
}