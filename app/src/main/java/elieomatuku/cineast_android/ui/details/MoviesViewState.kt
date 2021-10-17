package elieomatuku.cineast_android.ui.details

import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.ui.utils.SingleEvent
import elieomatuku.cineast_android.ui.utils.ViewError


/**
 * Created by elieomatuku on 2021-09-11
 */

data class MoviesViewState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val contents: List<Content>? = null,
    val genres: List<Genre>? = null,
    val viewError: SingleEvent<ViewError>? = null
)
