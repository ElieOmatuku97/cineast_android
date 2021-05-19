package elieomatuku.cineast_android.core.model

data class FavoriteListMedia(override val media_type: String, override val media_id: Int, val favorite: Boolean) : Media()
