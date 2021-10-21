package elieomatuku.cineast_android.remote.request

import elieomatuku.cineast_android.remote.RemoteUtils

/**
 * Created by elieomatuku on 2021-07-04
 */

data class FavouritesMediaRequest(
    val media_type: String = RemoteUtils.MOVIE,
    val media_id: Int,
    val favorite: Boolean
)
