package elieomatuku.restapipractice.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.restapipractice.business.business.model.data.Movie
import elieomatuku.restapipractice.viewholder.itemHolder.MovieItemHolder
import io.reactivex.subjects.PublishSubject

class SimilarAdapter(private val similarMovies: List<Movie>, private val onItemClickPublisher: PublishSubject<Movie>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int {
        return  similarMovies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MovieItemHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemMovieHolder = holder as MovieItemHolder
        val movie = similarMovies[position]
        itemMovieHolder.update(movie.poster_path, movie.release_date, movie.original_title)

        itemMovieHolder.itemView.setOnClickListener {
            onItemClickPublisher.onNext(movie)
        }
    }
}