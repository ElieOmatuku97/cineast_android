package elieomatuku.cineast_android.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.connection.ConnectionService
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.viewholder.EmptyStateItem
import org.kodein.di.generic.instance

/**
 * Created by elieomatuku on 2021-05-30
 */


const val GRID_VIEW_NUMBER_OF_COLUMNS = 2

abstract class ContentGridFragment : BaseFragment() {

    private val connectionService: ConnectionService by instance()
    private lateinit var composeView: ComposeView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        composeView = ComposeView(requireContext())
        return composeView
    }

    protected fun updateView(state: ContentGridViewState) {
        composeView.setContent {
            state.contents?.let { contents ->
                ContentGrid(contents = contents) {
                    gotoContent(it)
                }
            }

            state.viewError?.apply {
                EmptyStateItem(
                    errorMsg = peek().message,
                    hasNetworkConnection = connectionService.hasNetworkConnection
                )
            }
        }
    }

    protected abstract fun gotoContent(content: Content)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentGrid(contents: List<Content>, onContentClick: (content: Content) -> Unit) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(GRID_VIEW_NUMBER_OF_COLUMNS)
    ) {
        items(contents) { content ->
            ContentItem(content = content) {
                onContentClick(it)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ContentItem(content: Content, onContentClick: (content: Content) -> Unit) {
    val imagePath = when (content) {
        is Movie -> {
            content.posterPath
        }
        is Person -> {
            content.profilePath
        }
        else -> null
    }

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