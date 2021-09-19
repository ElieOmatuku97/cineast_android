package elieomatuku.cineast_android.ui.discover

import elieomatuku.cineast_android.domain.model.AccessToken
import elieomatuku.cineast_android.domain.model.DiscoverContents
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.utils.SingleEvent
import elieomatuku.cineast_android.utils.ViewError


/**
 * Created by elieomatuku on 2021-09-05
 */

data class DiscoverViewState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val discoverContents: DiscoverContents? = null,
    val viewError: SingleEvent<ViewError>? = null,
    val accessToken: SingleEvent<AccessToken>? = null,
    val genres: List<Genre>? = null,
)
