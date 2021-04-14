package elieomatuku.cineast_android.ui.common_viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.service.ConnectionService
import kotlinx.android.synthetic.main.holder_empty_state.view.*
import org.kodein.di.generic.instance


class EmptyStateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_empty_state, parent, false)
        }

        fun newInstance(parent: ViewGroup): EmptyStateHolder {
            return EmptyStateHolder(createView(parent))
        }
    }

    private val connectionService: ConnectionService by App.kodein.instance()


    private val msgView: TextView by lazy {
        itemView.empty_msg
    }

    private val msgTitle: TextView by lazy {
        itemView.empty_title
    }

    private val msgIcon: ImageView by lazy {
        itemView.empty_icon
    }

    fun update(errorMsg: String? = null) {

        if (connectionService.hasNetworkConnection) {
            msgTitle.text = itemView.resources.getText(R.string.no_content_title)
            msgView.text = errorMsg?.toUpperCase()
            msgIcon.setImageDrawable(ResourcesCompat.getDrawable(itemView.resources, R.drawable.ic_movie_black_24dp, itemView.context.theme))
        }
    }

}