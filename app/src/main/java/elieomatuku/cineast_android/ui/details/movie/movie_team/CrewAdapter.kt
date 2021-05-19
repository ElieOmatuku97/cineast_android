package elieomatuku.cineast_android.ui.details.movie.movie_team

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.core.model.Crew
import io.reactivex.subjects.PublishSubject

class CrewAdapter(private val crew: List<Crew>, private val onCrewClickPublisher: PublishSubject<Crew>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return crew.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CrewItemHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CrewItemHolder).update(crew[position])
        holder.itemView.setOnClickListener {
            onCrewClickPublisher.onNext(crew[position])
        }
    }
}
