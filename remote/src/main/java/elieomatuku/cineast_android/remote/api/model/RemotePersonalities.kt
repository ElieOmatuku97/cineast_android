package elieomatuku.cineast_android.remote.api.model

import androidx.annotation.Keep

@Keep
data class RemotePersonalities(
    val results: List<RemotePersonality> = listOf()
)

@Keep
data class RemotePersonality(
    val profile_path: String?,
    val adult: Boolean?,
    val id: Int,
    val name: String?
)
