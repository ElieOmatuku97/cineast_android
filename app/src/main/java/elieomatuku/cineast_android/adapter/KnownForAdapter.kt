package elieomatuku.cineast_android.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.model.data.Movie
import elieomatuku.cineast_android.model.data.KnownFor
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

        knownFor.id?.let {
            val movie = Movie(id = it, poster_path = knownFor.poster_path, release_date = knownFor.release_date, original_title = knownFor.original_title)

            itemMovieHolder.update(movie)

            itemMovieHolder.itemView.setOnClickListener {
                onItemClickPublisher.onNext(movie.id)
            }
        }
    }
}