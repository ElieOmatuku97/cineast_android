package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.TrailerEntity

@Keep
data class RemoteTrailers(
    val results: List<RemoteTrailer> = listOf()
) {
    companion object {
        fun toTrailerEntities(remoteTrailers: RemoteTrailers): List<TrailerEntity> {
            return remoteTrailers.results.map {
                it.let(RemoteTrailer::toTrailerEntity)
            }
        }
    }
}

@Keep
data class RemoteTrailer(
    val id: String?,
    val iso_639_1: String?,
    val iso_3166_1: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: Int?,
    val type: String?
) {
    companion object {
        fun toTrailerEntity(remoteTrailer: RemoteTrailer): TrailerEntity {
            return TrailerEntity(
                remoteTrailer.id,
                remoteTrailer.iso_639_1,
                remoteTrailer.iso_3166_1,
                remoteTrailer.key,
                remoteTrailer.name,
                remoteTrailer.site,
                remoteTrailer.size,
                remoteTrailer.type
            )
        }
    }
}
