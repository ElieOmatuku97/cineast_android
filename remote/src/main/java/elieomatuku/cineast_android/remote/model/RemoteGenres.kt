package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.GenreEntity

@Keep
data class RemoteGenres(val genres: List<RemoteGenre> = listOf()) {
    companion object {
        fun toGenreEntities(remoteGenres: RemoteGenres): List<GenreEntity> {
            return remoteGenres.genres.map {
                RemoteGenre.toGenreEntity(it)
            }
        }
    }
}

@Keep
data class RemoteGenre(val id: Int, val name: String) {
    companion object {
        fun toGenreEntity(remoteGenre: RemoteGenre): GenreEntity {
            return GenreEntity(
                remoteGenre.id,
                remoteGenre.name
            )
        }
    }
}
