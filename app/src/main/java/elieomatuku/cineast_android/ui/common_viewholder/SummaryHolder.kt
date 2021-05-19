package elieomatuku.cineast_android.ui.common_viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

abstract class SummaryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract val summaryTitleView: TextView

    abstract val summaryView: TextView

    fun update(summary: String?) {
        if (summary != null) {
            summaryView.text = summary
        }
    }
}
