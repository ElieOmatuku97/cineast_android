package elieomatuku.cineast_android.discover

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.widgets.MoviesWidget
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.extensions.Contents
import elieomatuku.cineast_android.viewholder.ContentHolder
import io.reactivex.subjects.PublishSubject

class MoviesHolder(
    val composeView: ComposeView,
    private val onMovieClickPublisher: PublishSubject<Movie>,
) : ContentHolder,
    RecyclerView.ViewHolder(composeView) {
    companion object {
        private fun createComposeView(parent: ViewGroup): ComposeView {
            return ComposeView(parent.context)
        }

        fun newInstance(
            parent: ViewGroup,
            onMovieClickPublisher: PublishSubject<Movie>
        ): MoviesHolder {
            return MoviesHolder(createComposeView(parent), onMovieClickPublisher)
        }
    }

    init {
        composeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
    }

    override fun update(content: List<Content>, titleRes: Int) {
        val title = itemView.context.getString(titleRes)
        composeView.setContent {
            AppCompatTheme {
                MoviesWidget(
                    movies = content as List<Movie>,
                    sectionTitle = title,
                    onItemClick = { content, _ ->
                        if (content is Movie) {
                            onMovieClickPublisher.onNext(content)
                        }
                    },
                    onSeeAllClick = {}
                )
            }
        }
    }

    override fun update(content: Contents) {
        update(content.value as List<Movie>, content.titleResources)
    }
}
