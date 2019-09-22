package elieomatuku.cineast_android.adapter

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.model.data.KnownFor
import elieomatuku.cineast_android.business.model.data.PeopleDetails
import elieomatuku.cineast_android.fragment.KnownForFragment
import elieomatuku.cineast_android.fragment.OverviewPeopleFragment
import elieomatuku.cineast_android.viewholder.MenuPeopleHolder
import elieomatuku.cineast_android.viewholder.itemHolder.ProfilePeopleHolder
import io.reactivex.subjects.PublishSubject

class PeopleItemAdapter(private val peopleDetails: PeopleDetails, private val peopleMovies: List<KnownFor>,
                        private val onProfileClickedPicturePublisher: PublishSubject<Int>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_PEOPLE_PROFILE = 0
        const val TYPE_MENU_PEOPLE = 1
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        return  when (position) {
            TYPE_PEOPLE_PROFILE ->  TYPE_PEOPLE_PROFILE
            TYPE_MENU_PEOPLE -> TYPE_MENU_PEOPLE
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  when (viewType) {
            TYPE_PEOPLE_PROFILE->  ProfilePeopleHolder.newInstance(parent, onProfileClickedPicturePublisher)
            TYPE_MENU_PEOPLE ->  MenuPeopleHolder.newInstance(parent)
            else -> throw RuntimeException("View Type does not exist.")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            TYPE_PEOPLE_PROFILE -> {
                val movieProfileHolder = holder as ProfilePeopleHolder
                movieProfileHolder.update(peopleDetails)
            }

            TYPE_MENU_PEOPLE -> {
                val menuPeopleHolder = holder as MenuPeopleHolder
                menuPeopleHolder.overviewSegmentBtn.setOnClickListener {
                    val activity =  it.context as FragmentActivity
                    val overviewFragment = OverviewPeopleFragment.newInstance(peopleDetails?.biography)
                    (activity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, overviewFragment).commit()

                }

                menuPeopleHolder.knownForSegmentBtn.setOnClickListener {
                    val activity =  it.context as FragmentActivity
                    val peopleFragment = KnownForFragment.newInstance(peopleMovies, peopleDetails.name)
                    (activity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, peopleFragment).commit()
                }
            }
        }
    }
}