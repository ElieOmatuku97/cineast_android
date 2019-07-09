package elieomatuku.cineast_android.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.viewholder.itemHolder.MovieItemHolder
import io.reactivex.subjects.PublishSubject


class MovieAdapter(private val movies: List<Movie>, private val onItemClickPublisher: PublishSubject<Movie>,
                   private val itemListLayoutRes: Int? = null, private val onMovieRemovedPublisher: PublishSubject<Movie>? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MovieItemHolder.newInstance(parent, itemListLayoutRes)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemMovieHolder = holder as MovieItemHolder
        val movie = movies[position]
        itemMovieHolder.update(movie.poster_path, movie.release_date, movie.title, movie.vote_average)

        itemMovieHolder.itemView.setOnClickListener {
            Log.d(MovieAdapter::class.java.simpleName, "CLICKED && movie:  ${movies[position]}")
            onItemClickPublisher.onNext(movies[position])
        }
    }


    fun deleteItem(position: Int) {
        val movie = movies[position]
        onMovieRemovedPublisher?.onNext(movie)
        (movies as MutableList).removeAt(position)
        notifyItemRemoved(position)
    }
}