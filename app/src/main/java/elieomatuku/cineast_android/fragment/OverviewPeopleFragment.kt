package elieomatuku.cineast_android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.viewholder.SummaryHolder

class OverviewPeopleFragment: Fragment() {
    companion object {
        const val OVERVIEW_PEOPLE_BIO = "overview_people_bio"

        fun newInstance(peopleBio: String?): OverviewPeopleFragment {
            val args = Bundle()
            args.putString(OVERVIEW_PEOPLE_BIO, peopleBio)

            val fragment = OverviewPeopleFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val peopleBio: String = arguments?.get(OVERVIEW_PEOPLE_BIO) as String
        val rootView = FrameLayout(this.activity)
        rootView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val holder = SummaryHolder.newInstance(rootView, R.layout.holder_bio)
        rootView.addView(holder.itemView)
        holder.update(peopleBio, R.string.biography)
        return rootView
    }
}