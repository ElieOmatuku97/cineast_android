package elieomatuku.cineast_android.data.model

import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.domain.model.Avatar
import elieomatuku.cineast_android.domain.model.Gavatar

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
) {
    companion object {
        fun toAccount(accountEntity: AccountEntity): Account {
            return Account(
                accountEntity.avatar?.let(AvatarEntity::toAvatar),
                accountEntity.id,
                accountEntity.iso6391,
                accountEntity.iso31661,
                accountEntity.name,
                accountEntity.includeAdult,
                accountEntity.username
            )
        }
    }
}

data class AvatarEntity(val gravatar: GAvatarEntity?) {
    companion object {
        fun toAvatar(avatarEntity: AvatarEntity): Avatar {
            return Avatar(
                avatarEntity.gravatar?.let(GAvatarEntity::toGAvatar)
            )
        }
    }
}

data class GAvatarEntity(val hash: String?) {
    companion object {
        fun toGAvatar(gAvatarEntity: GAvatarEntity): Gavatar {
            return Gavatar(
                gAvatarEntity.hash
            )
        }
    }
}
