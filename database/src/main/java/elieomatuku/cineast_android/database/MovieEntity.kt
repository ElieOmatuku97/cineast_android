package elieomatuku.cineast_android.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import elieomatuku.cineast_android.model.data.Movie


/**
 * Created by elieomatuku on 2019-12-06
 */

@Entity(tableName = MovieEntity.MOVIE_TABLE)
data class MovieEntity(
        @PrimaryKey val id: Int,
        val type: String,
        val poster_path: String? = null,
        val adult: Boolean = false,
        val overview: String? = null,
        val release_date: String? = null,
        val genre_ids: List<Int>? = null,
        val original_title: String? = null,
        val original_language: String? = null,
        val title: String? = null,
        val backdrop_path: String? = null,
        val popularity: Double? = null,
        val vote_count: Int? = null,
        val video: Boolean? = true,
        val vote_average: Float? = null,
        val rating: Float? = null
) {

    companion object {
        const val MOVIE_TABLE = "movie_table"

        fun fromPopularMovie(movie: Movie): MovieEntity {
            return fromMovie(MovieType.POPULAR, movie)
        }

        fun fromNowPlayingMovie(movie: Movie): MovieEntity {
            return fromMovie(MovieType.NOW_PLAYING, movie)
        }

        fun fromUpcomingMovie(movie: Movie): MovieEntity {
            return fromMovie(MovieType.UPCOMING, movie)
        }

        fun fromTopRated(movie: Movie): MovieEntity {
            return fromMovie(MovieType.TOP_RATED, movie)
        }

        fun fromMovie(movieType: MovieType, movie: Movie): MovieEntity {
            return MovieEntity(
                    id = movie.id,
                    type = movieType.type,
                    poster_path = movie.poster_path,
                    adult = movie.adult,
                    overview = movie.overview,
                    release_date = movie.release_date,
                    genre_ids = movie.genre_ids,
                    original_title = movie.original_title,
                    original_language = movie.original_language,
                    title = movie.title,
                    backdrop_path = movie.backdrop_path,
                    popularity = movie.popularity,
                    vote_count = movie.vote_count,
                    video = movie.video,
                    vote_average = movie.vote_average,
                    rating = movie.rating

            )
        }
    }
}

enum class MovieType(val type: String) {
    POPULAR(type = "popular"),
    NOW_PLAYING(type = "nowPlaying"),
    UPCOMING(type = "upcoming"),
    TOP_RATED(type = "topRated");


    fun fromType(type: String): MovieType {
        return values().first { it.type == type }
    }

}