package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.AccessTokenEntity

/**
 * Created by elieomatuku on 2021-07-04
 */

@Keep
data class RemoteAccessToken(
    val success: Boolean = false,
    val expires_at: String? = null,
    val request_token: String? = null
) {

    companion object {
        fun toAccessTokenEntity(remoteAccessToken: RemoteAccessToken): AccessTokenEntity {
            return AccessTokenEntity(
                remoteAccessToken.success,
                remoteAccessToken.expires_at,
                remoteAccessToken.request_token
            )
        }
    }
}
