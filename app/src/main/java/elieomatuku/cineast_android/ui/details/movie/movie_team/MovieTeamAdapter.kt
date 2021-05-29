package elieomatuku.cineast_android.ui.details.movie.movie_team

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Cast
import elieomatuku.cineast_android.core.model.Crew
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.ui.discover.PeopleHolder
import io.reactivex.subjects.PublishSubject

class MovieTeamAdapter(
    private val cast: List<Cast>,
    private val crew: List<Crew>,
    private val onPeopleClickPublisher: PublishSubject<Person>
) :
    RecyclerView.Adapter<PeopleHolder>() {
    companion object {
        const val TYPE_CAST = 0
        const val TYPE_CREW = 1
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            TYPE_CAST -> TYPE_CAST
            TYPE_CREW -> TYPE_CREW
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleHolder {
        return when (viewType) {
            TYPE_CAST, TYPE_CREW -> PeopleHolder.newInstance(parent, onPeopleClickPublisher)
            else -> throw RuntimeException("View Type does not exist.")
        }
    }

    override fun onBindViewHolder(holder: PeopleHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_CAST -> {
                holder.update(cast, R.string.cast)
            }
            TYPE_CREW -> {
                holder.update(crew, R.string.crew)
            }
        }
    }
}
