package elieomatuku.cineast_android.domain.model

data class CineastError(
    val statusMessage: String? = null,
    val statusCode: Integer? = null,
    val success: Boolean = true
)
