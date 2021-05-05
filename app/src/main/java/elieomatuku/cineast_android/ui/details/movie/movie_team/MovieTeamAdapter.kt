package elieomatuku.cineast_android.ui.details.movie.movie_team

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.core.model.Cast
import elieomatuku.cineast_android.core.model.Crew
import io.reactivex.subjects.PublishSubject


class MovieTeamAdapter(private val cast: List<Cast>, private val crew: List<Crew>, private val onCrewClickPublisher: PublishSubject<Crew>,
                       private val onCastClickPublisher: PublishSubject<Cast>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CAST -> CastHolder.newInstance(parent)
            TYPE_CREW -> CrewHolder.newInstance(parent)
            else -> throw RuntimeException("View Type does not exist.")

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            TYPE_CAST -> {
                val castHolder = holder as CastHolder
                castHolder.update(cast, onCastClickPublisher)
            }
            TYPE_CREW -> {
                val crewHolder = holder as CrewHolder
                crewHolder.update(crew, onCrewClickPublisher)
            }
        }
    }
}