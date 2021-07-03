package elieomatuku.cineast_android.ui.settings.user_movies

import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.ui.contents.ContentsAdapter
import io.reactivex.subjects.PublishSubject

class UserContentsAdapter(
    onItemClickPublisher: PublishSubject<Content>,
    itemListLayoutRes: Int? = null,
    private val onMovieRemovedPublisher: PublishSubject<Movie>? = null
) :
    ContentsAdapter(onItemClickPublisher, itemListLayoutRes) {

    fun deleteItem(position: Int) {
        val movie = contents[position]
        onMovieRemovedPublisher?.onNext(movie as Movie)
        (contents).removeAt(position)
        notifyItemRemoved(position)
    }
}
