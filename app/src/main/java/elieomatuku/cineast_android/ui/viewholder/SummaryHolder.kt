package elieomatuku.cineast_android.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import kotlinx.android.synthetic.main.holder_summary.view.*

/**
 * Created by elieomatuku on 2020-01-09
 */

class SummaryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_summary, parent, false)
        }

        fun newInstance(parent: ViewGroup): SummaryHolder {
            return SummaryHolder(createView(parent))
        }
    }

    private val summaryTitleView: TextView by lazy {
        val view = itemView.summary_title_view
        view
    }

    private val summaryView: TextView by lazy {
        itemView.summary_view
    }

    fun update(summaryTitle: Int?, summary: String?) {
        summaryTitle?.let {
            summaryTitleView.text = itemView.resources.getString(it)
        }

        summary?.let {
            summaryView.text = it
        }
    }
}
