package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep

/**
 * Created by elieomatuku on 2021-07-04
 */

@Keep
data class RemoteAccessToken(
    val success: Boolean = false,
    val expires_at: String? = null,
    val request_token: String? = null
)
