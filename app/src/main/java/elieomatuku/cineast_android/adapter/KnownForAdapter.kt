package elieomatuku.cineast_android.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.core.model.KnownFor
import elieomatuku.cineast_android.viewholder.itemHolder.MovieItemHolder
import io.reactivex.subjects.PublishSubject

class KnownForAdapter(private val knownFor: List<KnownFor>, private val onItemClickPublisher: PublishSubject<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return knownFor.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieItemHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemMovieHolder = holder as MovieItemHolder
        val knownFor = knownFor[position]
        val movie = knownFor.toMovie()

        movie?.let {
            itemMovieHolder.update(movie)

            itemMovieHolder.itemView.setOnClickListener {
                onItemClickPublisher.onNext(movie.id)
            }
        }
    }
}