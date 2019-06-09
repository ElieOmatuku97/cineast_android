package elieomatuku.cineast_android.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.business.business.model.data.Crew
import elieomatuku.cineast_android.viewholder.itemHolder.CrewItemHolder
import io.reactivex.subjects.PublishSubject

class CrewAdapter(private val crew: List<Crew>, private val onCrewClickPublisher: PublishSubject<Crew>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return crew.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CrewItemHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as  CrewItemHolder).update(crew[position])
        holder.itemView.setOnClickListener {
            onCrewClickPublisher.onNext(crew[position])
        }
    }
}