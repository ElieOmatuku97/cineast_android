package elieomatuku.cineast_android.data.model

import elieomatuku.cineast_android.domain.model.Genre

/**
 * Created by elieomatuku on 2021-08-08
 */

data class GenreEntity(
    val id: Int,
    val name: String
) {
    companion object {
        fun toGenre(genreEntity: GenreEntity): Genre {
            return Genre(
                genreEntity.id,
                genreEntity.name
            )
        }

        fun fromGenre(genre: Genre): GenreEntity {
            return GenreEntity(
                genre.id,
                genre.name
            )
        }
    }
}
