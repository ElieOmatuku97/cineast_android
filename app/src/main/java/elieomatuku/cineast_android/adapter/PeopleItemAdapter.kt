package elieomatuku.cineast_android.adapter

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.KnownFor
import elieomatuku.cineast_android.core.model.PersonalityDetails
import elieomatuku.cineast_android.fragment.KnownForFragment
import elieomatuku.cineast_android.fragment.OverviewPeopleFragment
import elieomatuku.cineast_android.viewholder.EmptyStateHolder
import elieomatuku.cineast_android.viewholder.MenuPeopleHolder
import elieomatuku.cineast_android.viewholder.itemHolder.ProfilePeopleHolder
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import kotlin.properties.Delegates

class PeopleItemAdapter(private val onProfileClickedPicturePublisher: PublishSubject<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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


    var peopleMovies: MutableList<KnownFor> by Delegates.observable(mutableListOf()) { prop, oldMovies, nuMovies ->
        Timber.d("widgets = $nuMovies")
        hasValidData = true
        errorMessage = null
    }

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

    val hasEmptyState: Boolean
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
            TYPE_MENU_PEOPLE -> MenuPeopleHolder.newInstance(parent)
            TYPE_EMPTY_STATE -> {
                EmptyStateHolder.newInstance(parent)
            }
            else -> throw RuntimeException("View Type does not exist.")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            TYPE_PEOPLE_PROFILE -> {
                val movieProfileHolder = holder as ProfilePeopleHolder
                movieProfileHolder.update(personalityDetails)
            }

            TYPE_MENU_PEOPLE -> {
                val menuPeopleHolder = holder as MenuPeopleHolder
                menuPeopleHolder.overviewSegmentBtn.setOnClickListener {
                    val activity = it.context as FragmentActivity
                    val overviewFragment = OverviewPeopleFragment.newInstance(personalityDetails)
                    (activity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, overviewFragment).commit()

                }

                menuPeopleHolder.knownForSegmentBtn.setOnClickListener {
                    val activity = it.context as FragmentActivity
                    val peopleFragment = KnownForFragment.newInstance(peopleMovies, personalityDetails.name)
                    (activity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, peopleFragment).commit()
                }
            }

            MovieAdapter.TYPE_EMPTY_STATE -> {
                (holder as EmptyStateHolder).update(errorMessage)
            }
        }
    }
}