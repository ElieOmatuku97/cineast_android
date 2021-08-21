package elieomatuku.cineast_android.domain.model

data class AccessToken(
    val success: Boolean = false,
    val expiresAt: String? = null,
    val requestToken: String? = null
)
