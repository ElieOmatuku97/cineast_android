package elieomatuku.cineast_android.remote.api.model

import androidx.annotation.Keep

@Keep
data class RemoteGenres(val genres: List<RemoteGenre> = listOf())

@Keep
data class RemoteGenre(val id: Int, val name: String)
