package elieomatuku.cineast_android.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import elieomatuku.cineast_android.data.model.GenreEntity

/**
 * Created by elieomatuku on 2019-12-06
 */

@Entity(tableName = CacheGenre.GENRE_TABLE)
data class CacheGenre(@PrimaryKey val id: Int, val name: String) {
    companion object {
        const val GENRE_TABLE = "genre_table"

        fun fromGenres(genres: List<GenreEntity>): List<CacheGenre> {
            return genres.map { fromGenre(it) }
        }

        private fun fromGenre(genre: GenreEntity): CacheGenre {
            return CacheGenre(genre.id, genre.name)
        }

        fun toGenres(cacheGenres: List<CacheGenre>): List<GenreEntity> {
            return cacheGenres.map { it.toGenre() }
        }
    }

    fun toGenre(): GenreEntity {
        return GenreEntity(id = id, name = name)
    }
}
