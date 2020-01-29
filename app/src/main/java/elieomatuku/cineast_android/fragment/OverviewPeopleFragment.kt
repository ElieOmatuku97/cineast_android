package elieomatuku.cineast_android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import elieomatuku.cineast_android.core.model.PersonalityDetails
import elieomatuku.cineast_android.viewholder.BiographyHolder


class OverviewPeopleFragment: Fragment() {
    companion object {
        const val OVERVIEW_PEOPLE_DETAILS = "overview_people_details"

        fun newInstance(personalityDetails: PersonalityDetails): OverviewPeopleFragment {
            val args = Bundle()
            args.putParcelable(OVERVIEW_PEOPLE_DETAILS, personalityDetails)

            val fragment = OverviewPeopleFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val peopleDetails: PersonalityDetails = arguments?.get(OVERVIEW_PEOPLE_DETAILS)  as PersonalityDetails
        val rootView = FrameLayout(this.activity)
        rootView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val holder = BiographyHolder.newInstance(rootView)
        rootView.addView(holder.itemView)
        val biography = peopleDetails.biography
        holder.update(biography)
        return rootView
    }
}