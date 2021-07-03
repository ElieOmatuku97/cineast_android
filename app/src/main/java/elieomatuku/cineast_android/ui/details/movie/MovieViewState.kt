package elieomatuku.cineast_android.ui.details.movie

import elieomatuku.cineast_android.core.model.MovieSummary
import elieomatuku.cineast_android.core.model.Poster
import elieomatuku.cineast_android.utils.ViewError

/**
 * Created by elieomatuku on 2021-07-03
 */

data class MovieViewState(
    val isLoading: Boolean = false,
    val movieSummary: MovieSummary? = null,
    val isInWatchList: Boolean = false,
    val isInFavorites: Boolean = false,
    val posters: List<Poster>? = listOf(),
    val viewError: ViewError? = null,
)
