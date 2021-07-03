package elieomatuku.cineast_android.domain.model

data class WatchListMedia(override val media_type: String, override val media_id: Int, val watchlist: Boolean) : Media()
