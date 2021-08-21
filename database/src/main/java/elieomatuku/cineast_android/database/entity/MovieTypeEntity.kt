package elieomatuku.cineast_android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by elieomatuku on 2019-12-08
 */

@Entity(tableName = MovieTypeEntity.MOVIE_TYPE_TABLE)
data class MovieTypeEntity(
    @PrimaryKey val id: String,
    val name: String
) {

    companion object {
        const val MOVIE_TYPE_TABLE = "movie_type_table"

        fun getPredefinedTypes(): List<MovieTypeEntity> {
            return MovieType.values().toList().map {
                toMovieTypeEntity(it)
            }
        }

        private fun toMovieTypeEntity(movieType: MovieType): MovieTypeEntity {
            return MovieTypeEntity(movieType.id, movieType.type)
        }
    }
}

enum class MovieType(val id: String, val type: String) {
    POPULAR("cineast_popular", "popular"),
    NOW_PLAYING("cineast_nowPlaying", "nowPlaying"),
    UPCOMING("cineast_upcoming", "upcoming"),
    TOP_RATED("cineast_topRated", "topRated");
}
