package elieomatuku.cineast_android.core.model


data class WatchListMedia (override val media_type: String, override val media_id: Int, val watchlist: Boolean): Media()

