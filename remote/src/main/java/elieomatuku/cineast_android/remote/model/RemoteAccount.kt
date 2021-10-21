package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.AccountEntity
import elieomatuku.cineast_android.data.model.AvatarEntity
import elieomatuku.cineast_android.data.model.GAvatarEntity

/**
 * Created by elieomatuku on 2021-07-04
 */

@Keep
data class RemoteAccount(
    val avatar: RemoteAvatar?,
    val id: Int?,
    val iso_639_1: String?,
    val iso_3166_1: String?,
    val name: String?,
    val include_adult: Boolean?,
    val username: String?
) {
    companion object {
        fun toAccountEntity(remoteAccount: RemoteAccount): AccountEntity {
            return AccountEntity(
                RemoteAvatar.toAvatarEntity(remoteAccount.avatar),
                remoteAccount.id,
                remoteAccount.iso_639_1,
                remoteAccount.iso_3166_1,
                remoteAccount.name,
                remoteAccount.include_adult,
                remoteAccount.username
            )
        }
    }
}

@Keep
data class RemoteAvatar(val gravatar: RemoteGavatar?) {
    companion object {
        fun toAvatarEntity(remoteAvatar: RemoteAvatar?): AvatarEntity {
            return AvatarEntity(
                GAvatarEntity(remoteAvatar?.gravatar?.hash)
            )
        }
    }
}

@Keep
data class RemoteGavatar(val hash: String?)
