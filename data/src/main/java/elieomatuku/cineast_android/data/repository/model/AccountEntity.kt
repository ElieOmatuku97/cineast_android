package elieomatuku.cineast_android.data.repository.model


/**
 * Created by elieomatuku on 2021-07-04
 */


data class AccountEntity(
    val avatar: AvatarEntity?,
    val id: Int?,
    val iso_639_1: String?,
    val iso_3166_1: String?,
    val name: String?,
    val includeAdult: Boolean?,
    val username: String?
)


data class AvatarEntity(val gravatar: GavatarEntity?)


data class GavatarEntity(val hash: String?)
