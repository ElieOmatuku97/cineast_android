package elieomatuku.cineast_android.ui.search

import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.utils.SingleEvent
import elieomatuku.cineast_android.utils.ViewError

/**
 * Created by elieomatuku on 2021-10-02
 */

data class SearchViewState(
    val isLoading: Boolean = false,
    val results: List<Content> = listOf(),
    val viewError: SingleEvent<ViewError>? = null,
)
