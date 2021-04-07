package elieomatuku.cineast_android.ui.viewholder


import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView


abstract class SummaryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract val summaryTitleView: TextView

    abstract val summaryView: TextView

    fun update(summary: String?) {
        if (summary != null) {
            summaryView.text = summary
        }
    }
}