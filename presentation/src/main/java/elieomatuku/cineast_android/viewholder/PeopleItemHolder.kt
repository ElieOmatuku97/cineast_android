package elieomatuku.cineast_android.viewholder

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.domain.model.Person

class PeopleItemHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView) {
    companion object {
        private fun createComposeView(parent: ViewGroup): ComposeView {
            return ComposeView(parent.context)
        }

        fun newInstance(parent: ViewGroup): PeopleItemHolder {
            return PeopleItemHolder(createComposeView(parent))
        }
    }

    fun update(actor: Person) {
        composeView.setContent {
            AppCompatTheme {
                ContentItem(imagePath = actor.profilePath, title = actor.name)
            }
        }
    }
}
