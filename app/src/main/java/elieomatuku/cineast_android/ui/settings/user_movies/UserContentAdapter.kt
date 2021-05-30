package elieomatuku.cineast_android.ui.settings.user_movies

import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.ui.adapter.ContentAdapter
import io.reactivex.subjects.PublishSubject

class UserContentAdapter(
    onItemClickPublisher: PublishSubject<Content>,
    itemListLayoutRes: Int? = null,
    private val onMovieRemovedPublisher: PublishSubject<Movie>? = null
) :
    ContentAdapter(onItemClickPublisher, itemListLayoutRes) {

    fun deleteItem(position: Int) {
        val movie = contents[position]
        onMovieRemovedPublisher?.onNext(movie as Movie)
        (contents).removeAt(position)
        notifyItemRemoved(position)
    }
}
