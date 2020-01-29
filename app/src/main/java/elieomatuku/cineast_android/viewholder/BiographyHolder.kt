package elieomatuku.cineast_android.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import kotlinx.android.synthetic.main.holder_summary.view.*


/**
 * Created by elieomatuku on 2020-01-09
 */

class BiographyHolder(itemView: View) : SummaryHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_bio, parent, false)
        }

        fun newInstance(parent: ViewGroup): BiographyHolder {
            return BiographyHolder(createView(parent))
        }
    }

    override val summaryTitleView by lazy {
        val view = itemView.summary_title_view
        view.text = itemView.resources.getString(R.string.biography)
        view
    }

    override val summaryView by lazy {
        itemView.summary_view
    }
}