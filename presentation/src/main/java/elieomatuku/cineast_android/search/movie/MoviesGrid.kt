package elieomatuku.cineast_android.search.movie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import elieomatuku.cineast_android.contents.ContentGrid
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.widgets.EmptyStateWidget

/**
 * Created by elieomatuku on 2021-06-05
 */

@Composable
fun MoviesGrid(
    viewModel: MoviesGridViewModel = hiltViewModel(),
    hasNetworkConnection: Boolean,
    onContentClick: (content: Content) -> Unit
) {
    val viewState by viewModel.viewState.observeAsState()

    viewState?.contents?.let { contents ->
        ContentGrid(contents = contents) {
            onContentClick(it)
        }
    }

    viewState?.viewError?.apply {
        EmptyStateWidget(
            errorMsg = peek().message,
            hasNetworkConnection = hasNetworkConnection
        )
    }
}
