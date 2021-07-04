package elieomatuku.cineast_android.remote.request


/**
 * Created by elieomatuku on 2021-07-04
 */

data class WatchListMediaRequest(val media_type: String, val media_id: Int, val watchlist: Boolean)
