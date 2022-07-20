package elieomatuku.cineast_android.details.movie.overview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject

class TrailersAdapter(private val onTrailClickedPublisher: PublishSubject<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var trailers: List<String> = listOf()
    var trailersVideosTitleMap: Map<String, String> = mapOf()

    override fun getItemCount(): Int {
        return trailers.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TrailerItemHolder.newInstance(parent, onTrailClickedPublisher)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TrailerItemHolder).update(
            trailers[position],
            trailersVideosTitleMap[trailers[position]]
        )
    }
}
