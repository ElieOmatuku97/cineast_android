package elieomatuku.cineast_android.viewholder

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.extensions.Contents
import elieomatuku.cineast_android.extensions.asListOfType
import elieomatuku.cineast_android.widgets.PeopleWidget
import io.reactivex.subjects.PublishSubject

class PeopleHolder(
    val composeView: ComposeView,
    private val onPeopleClickPublisher: PublishSubject<Content>,
    private val onSeeAllClickPublisher: PublishSubject<Pair<List<Content>, Int>>
) :
    ContentHolder, RecyclerView.ViewHolder(composeView) {

    companion object {
        private fun createComposeView(parent: ViewGroup): ComposeView {
            return ComposeView(parent.context)
        }

        fun newInstance(
            parent: ViewGroup,
            onPersonalityClickPublisher: PublishSubject<Content>,
            onSeeAllClickPublisher: PublishSubject<Pair<List<Content>, Int>>
        ): PeopleHolder {
            return PeopleHolder(
                createComposeView(parent),
                onPersonalityClickPublisher,
                onSeeAllClickPublisher
            )
        }
    }

    override fun update(content: Contents) {
        update(content.value ?: emptyList(), content.titleResources)
    }

    override fun update(content: List<Content>, titleRes: Int) {
        composeView.setContent {
            AppCompatTheme {
                PeopleWidget(
                    people = content.asListOfType() ?: emptyList(),
                    sectionTitle = composeView.context.getString(titleRes),
                    onItemClick = { onPeopleClickPublisher.onNext(it) }
                ) {
                    val pair = Pair(content, titleRes)
                    onSeeAllClickPublisher.onNext(pair)
                }
            }
        }
    }
}
