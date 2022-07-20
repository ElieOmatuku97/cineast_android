package elieomatuku.cineast_android.discover

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.widgets.MoviesWidget
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.extensions.Contents
import elieomatuku.cineast_android.extensions.asListOfType
import elieomatuku.cineast_android.viewholder.ContentHolder
import io.reactivex.subjects.PublishSubject
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MoviesHolder(
    val composeView: ComposeView,
    private val onMovieClickPublisher: PublishSubject<Movie>,
) : ContentHolder,
    RecyclerView.ViewHolder(composeView), KodeinAware {
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

    override val kodein: Kodein by kodein(itemView.context)
    private val viewModelFactory: ViewModelProvider.Factory by instance()

    override fun update(content: List<Content>, titleRes: Int) {
        val title = itemView.context.getString(titleRes)
        composeView.setContent {
            AppCompatTheme {
                MoviesWidget(
                    viewModelFactory = viewModelFactory,
                    movies = content.asListOfType() ?: emptyList(),
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
        update(content.value?.asListOfType<Movie>() ?: emptyList(), content.titleResources)
    }
}
