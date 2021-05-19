package elieomatuku.cineast_android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import elieomatuku.cineast_android.core.model.Genre

/**
 * Created by elieomatuku on 2019-12-06
 */

@Entity(tableName = GenreEntity.GENRE_TABLE)
data class GenreEntity(@PrimaryKey val id: Int, val name: String) {
    companion object {
        const val GENRE_TABLE = "genre_table"

        fun fromGenres(genres: List<Genre>): List<GenreEntity> {
            return genres.map { fromGenre(it) }
        }

        fun fromGenre(genre: Genre): GenreEntity {
            return GenreEntity(genre.id, genre.name)
        }

        fun toGenres(genreEntities: List<GenreEntity>): List<Genre> {
            return genreEntities.map { it.toGenre() }
        }
    }

    fun toGenre(): Genre {
        return Genre(id = id, name = name)
    }
}
