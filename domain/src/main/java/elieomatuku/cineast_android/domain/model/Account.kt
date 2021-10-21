package elieomatuku.cineast_android.domain.model

data class Account(
    val avatar: Avatar?,
    val id: Int?,
    val iso6391: String?,
    val iso31661: String?,
    val name: String?,
    val includeAdult: Boolean?,
    val username: String?
)

data class Avatar(val grAvatar: Gavatar?)

data class Gavatar(val hash: String?)
