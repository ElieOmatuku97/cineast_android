package elieomatuku.cineast_android.business.model.data


data class WatchListMedia (override val media_type: String, override val media_id: Int, val watchlist: Boolean): Media()

