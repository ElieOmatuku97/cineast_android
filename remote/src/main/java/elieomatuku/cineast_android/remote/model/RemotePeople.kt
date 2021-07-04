package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep

@Keep
data class RemotePeople(
    val results: List<RemotePerson> = listOf()
)

@Keep
data class RemotePerson(
    val profile_path: String?,
    val adult: Boolean?,
    val id: Int,
    val name: String?
)
