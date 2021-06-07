package elieomatuku.cineast_android.ui.details.people.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.PersonalityDetails
import elieomatuku.cineast_android.ui.viewholder.SummaryHolder

class BareOverviewFragment : Fragment() {
    companion object {
        const val OVERVIEW_PEOPLE_DETAILS = "overview_people_details"
        private const val OVERVIEW = "overview"

        fun newInstance(personalityDetails: PersonalityDetails): BareOverviewFragment {
            val args = Bundle()
            args.putParcelable(OVERVIEW_PEOPLE_DETAILS, personalityDetails)

            val fragment = BareOverviewFragment()
            fragment.arguments = args
            return fragment
        }


        fun newInstance(overview: String): BareOverviewFragment {
            val args = Bundle()
            args.putString(OVERVIEW, overview)

            val fragment = BareOverviewFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val peopleDetails: PersonalityDetails = arguments?.get(OVERVIEW_PEOPLE_DETAILS) as PersonalityDetails
        val rootView = FrameLayout(requireContext())
        rootView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val summaryHolder = SummaryHolder.newInstance(rootView)
        rootView.addView(summaryHolder.itemView)
        val biography = peopleDetails.biography
        summaryHolder.update(R.string.biography, biography)
        return rootView
    }
}

