package elieomatuku.cineast_android.ui.details.movie

import elieomatuku.cineast_android.domain.model.Image
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.ui.utils.SingleEvent
import elieomatuku.cineast_android.ui.utils.ViewError

/**
 * Created by elieomatuku on 2021-07-03
 */

data class MovieViewState(
    val isLoading: Boolean = false,
    val isLoggedIn: SingleEvent<Boolean>? = null,
    val movieSummary: MovieSummary? = null,
    val screenName: String? = null,
    val isInWatchList: Boolean = false,
    val isInFavorites: Boolean = false,
    val posters: List<Image>? = listOf(),
    val viewError: SingleEvent<ViewError>? = null
)
