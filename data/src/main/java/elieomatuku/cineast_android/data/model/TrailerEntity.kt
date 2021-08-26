package elieomatuku.cineast_android.data.model

import elieomatuku.cineast_android.domain.model.Trailer

/**
 * Created by elieomatuku on 2021-08-08
 */

data class TrailerEntity(
    val id: String?,
    val iso6391: String?,
    val iso31661: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: Int?,
    val type: String?
) {
    companion object {
        fun toTrailer(trailerEntity: TrailerEntity): Trailer {
            return Trailer(
                id = trailerEntity.id,
                iso6391 = trailerEntity.iso6391,
                iso31661 = trailerEntity.iso31661,
                key = trailerEntity.key,
                name = trailerEntity.name,
                site = trailerEntity.site,
                size = trailerEntity.size,
                type = trailerEntity.type
            )
        }

        fun fromTrailer(trailer: Trailer): TrailerEntity {
            return TrailerEntity(
                id = trailer.id,
                iso6391 = trailer.iso6391,
                iso31661 = trailer.iso31661,
                key = trailer.key,
                name = trailer.name,
                site = trailer.site,
                size = trailer.size,
                type = trailer.type
            )
        }
    }
}
