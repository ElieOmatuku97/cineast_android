package elieomatuku.cineast_android.ui.details.movie.overview

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.ui.viewholder.itemHolder.TrailerItemHolder

class TrailersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var trailers: List<String> = listOf()
    var trailersVideosTitleMap: Map<String, String> = mapOf()

    override fun getItemCount(): Int {
        return trailers.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TrailerItemHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TrailerItemHolder).update(trailers[position], trailersVideosTitleMap[trailers.get(position)])
    }
}