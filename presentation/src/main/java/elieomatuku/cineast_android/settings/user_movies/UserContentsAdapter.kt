package elieomatuku.cineast_android.settings.user_movies

import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.contents.ContentsAdapter
import io.reactivex.subjects.PublishSubject

class UserContentsAdapter(
    onItemClickPublisher: PublishSubject<Content>,
    private val onMovieRemovedPublisher: PublishSubject<Movie>? = null
) : ContentsAdapter(onItemClickPublisher) {

    fun deleteItem(position: Int) {
        val movie = contents[position]
        onMovieRemovedPublisher?.onNext(movie as Movie)
        (contents).removeAt(position)
        notifyItemRemoved(position)
    }
}
