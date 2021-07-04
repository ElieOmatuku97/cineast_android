package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep

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
)

@Keep
data class RemoteAvatar(val gravatar: RemoteGavatar?)

@Keep
data class RemoteGavatar(val hash: String?)
