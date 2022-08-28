package elieomatuku.cineast_android.discover

import android.view.ViewGroup
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.recyclerview.widget.RecyclerView
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.extensions.DiscoverWidget
import elieomatuku.cineast_android.extensions.asListOfType
import elieomatuku.cineast_android.viewholder.ContentHolder
import io.reactivex.subjects.PublishSubject

class HeaderHolder(
    val composeView: ComposeView,
    private val onItemClickPublisher: PublishSubject<Movie>
) :
    ContentHolder, RecyclerView.ViewHolder(composeView) {
    companion object {
        private fun createComposeView(parent: ViewGroup): ComposeView {
            return ComposeView(parent.context)
        }

        fun newInstance(
            parent: ViewGroup,
            onItemClickPublisher: PublishSubject<Movie>
        ): HeaderHolder {
            return HeaderHolder(createComposeView(parent), onItemClickPublisher)
        }
    }

    override fun update(content: DiscoverWidget) {
//        update(content.value ?: emptyList(), 0)
    }

    override fun update(content: List<Content>, titleRes: Int) {
        composeView.setContent {
            AppCompatTheme {
                LazyRow(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.holder_header_item_height))
                ) {
                    items(content.asListOfType<Movie>() ?: emptyList()) { movie ->
                        HeaderItem(movie = movie) {
                            onItemClickPublisher.onNext(it)
                        }
                    }
                }
            }
        }
    }
}
