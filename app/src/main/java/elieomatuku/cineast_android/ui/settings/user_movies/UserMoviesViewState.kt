package elieomatuku.cineast_android.ui.settings.user_movies

import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.ui.utils.SingleEvent
import elieomatuku.cineast_android.ui.utils.ViewError

/**
 * Created by elieomatuku on 2021-10-10
 */

data class UserMoviesViewState(
    val isLoading: Boolean = false,
    val viewError: SingleEvent<ViewError>? = null,
    val genres: List<Genre> = emptyList(),
    val userMovies: List<Content> = emptyList(),
)
