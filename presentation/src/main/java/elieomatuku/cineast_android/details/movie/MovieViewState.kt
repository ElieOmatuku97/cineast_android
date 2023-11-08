package elieomatuku.cineast_android.details.movie

import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.utils.SingleEvent
import elieomatuku.cineast_android.utils.ViewError

/**
 * Created by elieomatuku on 2021-07-03
 */

data class MovieViewState(
    val isLoading: Boolean = false,
    val screenName: String? = null,
    val viewError: SingleEvent<ViewError>? = null,
    val movie: Movie? = null
)