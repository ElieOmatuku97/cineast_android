package elieomatuku.cineast_android.discover

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.extensions.Contents
import elieomatuku.cineast_android.details.MoviesWidget
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
                MoviesWidget(content as List<Movie>, title,
                    onItemClick = {
                        if (it is Movie) {
                            onMovieClickPublisher.onNext(it)
                        }
                    },
                    onSeeAllClick = {}
                )
            }
        }
    }

    override fun update(content: Contents) {
        val title = itemView.context.getString(content.titleResources)
        composeView.setContent {
            AppCompatTheme {
                MoviesWidget(content.value as List<Movie>, title,
                    onItemClick = {
                        if (it is Movie) {
                            onMovieClickPublisher.onNext(it)
                        }
                    },
                    onSeeAllClick = {}
                )
            }
        }
    }
}
