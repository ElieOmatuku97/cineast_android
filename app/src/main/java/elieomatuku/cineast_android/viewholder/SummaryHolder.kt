package elieomatuku.cineast_android.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import kotlinx.android.synthetic.main.holder_plot.view.*

class SummaryHolder(itemView: View): RecyclerView.ViewHolder (itemView) {
    companion object {
        fun createView(parent: ViewGroup, layoutRes: Int? = null): View {
            return LayoutInflater.from(parent.context).inflate(layoutRes?: R.layout.holder_plot, parent, false)
        }

        fun newInstance(parent: ViewGroup, layoutRes: Int? = null): SummaryHolder {
            return SummaryHolder(createView(parent, layoutRes))
        }
    }

    val summaryTitleView by lazy {
        itemView.summary_title_view
    }

    val summaryView by lazy {
        itemView.summary_view
    }

    fun update(moviePlot: String? = null, summaryTitle: String? = null) {
        if (summaryTitle != null) {
            summaryTitleView.text = summaryTitle
        }

        if (moviePlot != null) {
            summaryView.text = moviePlot
        }
    }
}