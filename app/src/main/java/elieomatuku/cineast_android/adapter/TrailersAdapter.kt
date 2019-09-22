package elieomatuku.cineast_android.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.viewholder.itemHolder.TrailerItemHolder

class TrailersAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var trailers: List<String> = listOf()
    var trailersVideosTitleMap: Map<String, String> = mapOf()

    override fun getItemCount(): Int {
        return trailers.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TrailerItemHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val trailerItemHolder = holder as TrailerItemHolder
        holder.update(trailers[position], trailersVideosTitleMap[trailers.get(position)])
    }
}