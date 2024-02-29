package elieomatuku.cineast_android.presentation.settings.usercontents

import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.presentation.utils.SingleEvent
import elieomatuku.cineast_android.presentation.utils.ViewError

/**
 * Created by elieomatuku on 2021-10-10
 */

data class UserContentsViewState(
    val isLoading: Boolean = false,
    val viewError: SingleEvent<ViewError>? = null,
    val genres: List<Genre> = emptyList(),
    val contents: List<Content> = emptyList(),
)
