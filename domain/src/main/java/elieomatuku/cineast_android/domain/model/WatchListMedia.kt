package elieomatuku.cineast_android.domain.model

data class WatchListMedia(
    override val mediaType: String,
    override val mediaId: Int,
    val watchlist: Boolean
) : Media()
