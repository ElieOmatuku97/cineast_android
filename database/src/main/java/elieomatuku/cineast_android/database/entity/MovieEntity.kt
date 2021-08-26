package elieomatuku.cineast_android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import elieomatuku.cineast_android.domain.model.Movie

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
                poster_path = movie.posterPath,
                adult = movie.adult,
                overview = movie.overview,
                release_date = movie.releaseDate,
                genre_ids = movie.genreIds ?: listOf(),
                original_title = movie.originalTitle,
                original_language = movie.originalLanguage,
                title = movie.title,
                backdrop_path = movie.backdropPath,
                popularity = movie.popularity,
                vote_count = movie.voteCount,
                video = movie.video,
                vote_average = movie.voteAverage,
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
            posterPath = poster_path,
            adult = adult,
            overview = overview,
            releaseDate = release_date,
            genreIds = genre_ids,
            originalTitle = original_title,
            originalLanguage = original_language,
            title = title,
            backdropPath = backdrop_path,
            popularity = popularity,
            voteCount = vote_count,
            video = video,
            voteAverage = vote_average,
            rating = rating
        )
    }
}
