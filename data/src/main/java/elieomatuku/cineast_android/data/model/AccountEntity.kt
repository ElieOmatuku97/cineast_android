package elieomatuku.cineast_android.data.model


/**
 * Created by elieomatuku on 2021-07-04
 */


data class AccountEntity(
    val avatar: AvatarEntity?,
    val id: Int?,
    val iso6391: String?,
    val iso31661: String?,
    val name: String?,
    val includeAdult: Boolean?,
    val username: String?
)


data class AvatarEntity(val gravatar: GAvatarEntity?)


data class GAvatarEntity(val hash: String?)
