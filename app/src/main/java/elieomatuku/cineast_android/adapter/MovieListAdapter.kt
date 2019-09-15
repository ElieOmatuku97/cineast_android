package elieomatuku.cineast_android.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.viewholder.itemHolder.MovieItemHolder
import io.reactivex.subjects.PublishSubject


open class MovieListAdapter(private val movies: List<Movie>, private val onItemClickPublisher: PublishSubject<Movie>,
                       private val itemListLayoutRes: Int? = null): RecyclerView.Adapter<MovieItemHolder>(){

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemHolder {
        return  MovieItemHolder.newInstance(parent, itemListLayoutRes)
    }


    override fun onBindViewHolder(holder: MovieItemHolder, position: Int) {
        val movie = movies[position]
        holder.update(movie)

        holder.itemView.setOnClickListener {
            Log.d(MovieListAdapter::class.java.simpleName, "CLICKED && movie:  ${movies[position]}")
            onItemClickPublisher.onNext(movies[position])
        }
    }

}