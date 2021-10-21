package elieomatuku.cineast_android.data.model

import elieomatuku.cineast_android.domain.model.AccessToken

/**
 * Created by elieomatuku on 2021-07-04
 */

data class AccessTokenEntity(
    val success: Boolean = false,
    val expiresAt: String? = null,
    val requestToken: String? = null
) {
    companion object {
        fun toAccessToken(accessTokenEntity: AccessTokenEntity): AccessToken {
            return AccessToken(
                accessTokenEntity.success,
                accessTokenEntity.expiresAt,
                accessTokenEntity.requestToken
            )
        }
    }
}
