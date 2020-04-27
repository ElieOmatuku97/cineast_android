package elieomatuku.cineast_android.viewholder

import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.PersonalityDetails
import elieomatuku.cineast_android.vu.PeopleVu
import info.hoang8f.android.segmented.SegmentedGroup
import kotlinx.android.synthetic.main.holder_menu_people.view.*

class PeopleSegmentedButtonHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_menu_people, parent, false)
        }

        fun newInstance(parent: ViewGroup): PeopleSegmentedButtonHolder {
            return PeopleSegmentedButtonHolder(createView(parent))
        }
    }


    private val segmentedGroup: SegmentedGroup by lazy {
        itemView.segmented_group
    }

    val overviewSegmentBtn: AppCompatRadioButton by lazy {
        itemView.overview
    }

    val knownForSegmentBtn: AppCompatRadioButton by lazy {
        itemView.known_for_view
    }

    fun update(peopleDetails: PersonalityDetails?, checkedTab: String) {
        if (peopleDetails != null && !peopleDetails.isEmpty() ) {
            segmentedGroup.visibility = View.VISIBLE
        } else {
            segmentedGroup.visibility = View.GONE
        }

        when(checkedTab) {
            PeopleVu.OVERVIEW -> overviewSegmentBtn.isChecked = true
            PeopleVu.KNOWN_FOR -> knownForSegmentBtn.isChecked = true
        }
    }
}