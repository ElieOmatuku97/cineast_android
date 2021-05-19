package elieomatuku.cineast_android.ui.details.movie.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.ui.common_viewholder.SummaryHolder
import kotlinx.android.synthetic.main.holder_summary.view.*

/**
 * Created by elieomatuku on 2020-01-09
 */

class PlotSummaryHolder(itemView: View) : SummaryHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_summary, parent, false)
        }

        fun newInstance(parent: ViewGroup): PlotSummaryHolder {
            return PlotSummaryHolder(createView(parent))
        }
    }

    override val summaryTitleView by lazy {
        val view = itemView.summary_title_view
        view.text = itemView.resources.getString(R.string.plot_summary)
        view
    }

    override val summaryView by lazy {
        itemView.summary_view
    }
}
