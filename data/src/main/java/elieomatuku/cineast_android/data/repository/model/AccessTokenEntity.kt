package elieomatuku.cineast_android.data.repository.model


/**
 * Created by elieomatuku on 2021-07-04
 */


data class AccessTokenEntity(
    val success: Boolean = false,
    val expiresAt: String? = null,
    val requestToken: String? = null
)
