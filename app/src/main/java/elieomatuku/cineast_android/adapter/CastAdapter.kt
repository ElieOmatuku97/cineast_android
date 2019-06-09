package elieomatuku.restapipractice.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.restapipractice.business.business.model.data.Cast
import elieomatuku.restapipractice.viewholder.itemHolder.CastItemHolder
import io.reactivex.subjects.PublishSubject

class CastAdapter(private val cast: List<Cast>, private val onCastClickPublisher: PublishSubject<Cast>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return cast.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CastItemHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as  CastItemHolder).update(cast[position])
        holder.itemView.setOnClickListener {
            onCastClickPublisher.onNext(cast[position])
        }
    }
}