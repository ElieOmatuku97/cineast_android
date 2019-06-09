package elieomatuku.restapipractice.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import elieomatuku.restapipractice.business.business.model.data.Movie
import elieomatuku.restapipractice.viewholder.itemHolder.MovieItemHolder
import io.reactivex.subjects.PublishSubject


class MovieAdapter(private val movies: List<Movie>, private val onItemClickPublisher: PublishSubject<Movie>,
                   private val itemListLayoutRes: Int? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
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
}