package elieomatuku.cineast_android.adapter


import elieomatuku.cineast_android.core.model.Movie
import io.reactivex.subjects.PublishSubject


class UserMoviesAdapter(onItemClickPublisher: PublishSubject<Movie>,
                        itemListLayoutRes: Int? = null, private val onMovieRemovedPublisher: PublishSubject<Movie>? = null)
    : MoviesAdapter(onItemClickPublisher, itemListLayoutRes) {


    fun deleteItem(position: Int) {
        val movie = movies[position]
        onMovieRemovedPublisher?.onNext(movie)
        (movies).removeAt(position)
        notifyItemRemoved(position)  
    }
}