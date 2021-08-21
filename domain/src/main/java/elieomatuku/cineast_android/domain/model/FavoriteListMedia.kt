package elieomatuku.cineast_android.domain.model

data class FavoriteListMedia(override val mediaType: String, override val mediaId: Int, val favorite: Boolean) : Media()
