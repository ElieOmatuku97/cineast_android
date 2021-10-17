package elieomatuku.cineast_android.ui.settings

import elieomatuku.cineast_android.domain.model.AccessToken
import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.domain.model.Session
import elieomatuku.cineast_android.ui.utils.SingleEvent
import elieomatuku.cineast_android.ui.utils.ViewError


/**
 * Created by elieomatuku on 2021-10-17
 */

data class SettingsViewState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = true,
    val userName: String? = null,
    val requestToken: SingleEvent<String>? = null,
    val accessToken: SingleEvent<AccessToken>? = null,
    val session: Session? = null,
    val account: Account? = null,
    val viewError: SingleEvent<ViewError>? = null,
)
