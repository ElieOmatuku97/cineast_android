package elieomatuku.cineast_android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import elieomatuku.cineast_android.core.model.Movie


/**
 * Created by elieomatuku on 2019-12-06
 */

@Entity(tableName = MovieEntity.MOVIE_TABLE)
data class MovieEntity(
        @PrimaryKey val id: Int,
        val poster_path: String? = null,
        val adult: Boolean = false,
        val overview: String? = null,
        val release_date: String? = null,
        val genre_ids: List<Int> = listOf(),
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


        fun fromMovies(movies: List<Movie>): List<MovieEntity> {
            return movies.map { fromMovie(it) }
        }

        fun fromMovie(movie: Movie): MovieEntity {
            return MovieEntity(
                    id = movie.id,
                    poster_path = movie.poster_path,
                    adult = movie.adult,
                    overview = movie.overview,
                    release_date = movie.release_date,
                    genre_ids = movie.genre_ids ?: listOf(),
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

        fun toMovies(movieEntities: List<MovieEntity>): List<Movie> {
            return movieEntities.map { it.toMovie() }
        }
    }


    fun toMovie(): Movie {
        return Movie(
                id = id,
                poster_path = poster_path,
                adult = adult,
                overview = overview,
                release_date = release_date,
                genre_ids = genre_ids,
                original_title = original_title,
                original_language = original_language,
                title = title,
                backdrop_path = backdrop_path,
                popularity = popularity,
                vote_count = vote_count,
                video = video,
                vote_average = vote_average,
                rating = rating
        )
    }
}

