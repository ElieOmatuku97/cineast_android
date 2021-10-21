package elieomatuku.cineast_android.details.person

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.PersonDetails
import kotlinx.android.synthetic.main.holder_menu_people.view.*

class PersonSegmentedButtonHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_menu_people, parent, false)
        }

        fun newInstance(parent: ViewGroup): PersonSegmentedButtonHolder {
            return PersonSegmentedButtonHolder(createView(parent))
        }
    }

    private val segmentedGroup: RadioGroup by lazy {
        itemView.segmented_group
    }

    val overviewSegmentBtn: RadioButton by lazy {
        itemView.overview
    }

    val knownForSegmentBtn: RadioButton by lazy {
        itemView.known_for_view
    }

    fun update(peopleDetails: PersonDetails?, checkedTab: String) {
        if (peopleDetails != null && !peopleDetails.isEmpty()) {
            segmentedGroup.visibility = View.VISIBLE
        } else {
            segmentedGroup.visibility = View.GONE
        }

        when (checkedTab) {
            PersonActivity.OVERVIEW -> overviewSegmentBtn.isChecked = true
            PersonActivity.KNOWN_FOR -> knownForSegmentBtn.isChecked = true
        }
    }
}
