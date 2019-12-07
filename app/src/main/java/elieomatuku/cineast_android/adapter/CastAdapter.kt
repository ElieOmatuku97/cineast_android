package elieomatuku.cineast_android.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.model.data.Cast
import elieomatuku.cineast_android.viewholder.itemHolder.CastItemHolder
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