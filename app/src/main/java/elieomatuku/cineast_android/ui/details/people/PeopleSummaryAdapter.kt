package elieomatuku.cineast_android.ui.details.people

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.core.model.PersonalityDetails
import elieomatuku.cineast_android.ui.common_viewholder.EmptyStateHolder
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import kotlin.properties.Delegates

class PeopleSummaryAdapter(
    private val onProfileClickedPicturePublisher: PublishSubject<Int>,
    private val segmentedButtonPublisher: PublishSubject<Pair<String, PersonalityDetails>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_PEOPLE_PROFILE = 0
        const val TYPE_MENU_PEOPLE = 1
        const val TYPE_EMPTY_STATE = -2
    }

    var personalityDetails: PersonalityDetails by Delegates.observable(PersonalityDetails()) { prop, oldPeopleDetails, nuPeopleDetails ->
        Timber.d("peopleApi facts = $nuPeopleDetails")
        hasValidData = true
        errorMessage = null
    }

    private var initialCheckedTab: String = PeopleVu.OVERVIEW

    var hasValidData = false
        private set

    private var _errorMessage: String? = null
    var errorMessage: String?
        get() = _errorMessage
        set(nuErrorMessage) {
            Timber.d("from MovieListAdapter: $nuErrorMessage")
            _errorMessage = nuErrorMessage
            hasValidData = true
        }

    private val hasEmptyState: Boolean
        // only display empty state after valid data is set
        get() = hasValidData && (personalityDetails.isEmpty())

    override fun getItemCount(): Int {
        Timber.d("hasEmptyState: $hasEmptyState")
        return if (hasEmptyState) {
            1
        } else {
            2
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasEmptyState) {
            TYPE_EMPTY_STATE
        } else {
            when (position) {
                TYPE_PEOPLE_PROFILE -> TYPE_PEOPLE_PROFILE
                TYPE_MENU_PEOPLE -> TYPE_MENU_PEOPLE
                else -> -1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PEOPLE_PROFILE -> ProfilePeopleHolder.newInstance(parent, onProfileClickedPicturePublisher)
            TYPE_MENU_PEOPLE -> PeopleSegmentedButtonHolder.newInstance(parent)
            TYPE_EMPTY_STATE -> {
                EmptyStateHolder.newInstance(parent)
            }
            else -> throw RuntimeException("View Type does not exist.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyStateHolder -> holder.update(errorMessage)
            is ProfilePeopleHolder -> holder.update(personalityDetails)
            is PeopleSegmentedButtonHolder -> {
                holder.update(personalityDetails, initialCheckedTab)

                holder.overviewSegmentBtn.setOnClickListener {
                    segmentedButtonPublisher.onNext(Pair(PeopleVu.OVERVIEW, personalityDetails))
                }

                holder.knownForSegmentBtn.setOnClickListener {
                    segmentedButtonPublisher.onNext(Pair(PeopleVu.KNOWN_FOR, personalityDetails))
                }
            }
        }
    }
}
