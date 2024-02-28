package elieomatuku.cineast_android.settings

import elieomatuku.cineast_android.domain.model.AccessToken
import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.domain.model.Session
import elieomatuku.cineast_android.utils.SingleEvent
import elieomatuku.cineast_android.utils.ViewError


/**
 * Created by elieomatuku on 2021-10-17
 */

data class SettingsViewState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val userName: String? = null,
    val accessToken: SingleEvent<AccessToken>? = null,
    val requestToken: SingleEvent<String?>? = null,
    val session: Session? = null,
    val account: Account? = null,
    val viewError: SingleEvent<ViewError>? = null,
)
