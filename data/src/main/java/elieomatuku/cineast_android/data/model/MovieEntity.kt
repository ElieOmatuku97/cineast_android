package elieomatuku.cineast_android.data.model

import elieomatuku.cineast_android.domain.model.Movie

/**
 * Created by elieomatuku on 2021-08-08
 */

data class MovieEntity(
    val posterPath: String? = null,
    val adult: Boolean = false,
    val overview: String? = null,
    val releaseDate: String? = null,
    val genreIds: List<Int>? = listOf(),
    val genres: List<GenreEntity>? = listOf(),
    val id: Int,
    val originalTitle: String? = null,
    val originalLanguage: String? = null,
    val title: String? = null,
    val backdropPath: String? = null,
    val popularity: Double? = null,
    val voteCount: Int? = null,
    val video: Boolean? = true,
    val voteAverage: Float? = null,
    val rating: Float? = null
) {
    companion object {
        fun toMovie(movieEntity: MovieEntity): Movie {
            return Movie(
                posterPath = movieEntity.posterPath,
                adult = movieEntity.adult,
                overview = movieEntity.overview,
                releaseDate = movieEntity.releaseDate,
                genreIds = movieEntity.genreIds,
                genres = movieEntity.genres?.map { it.let(GenreEntity::toGenre) },
                id = movieEntity.id,
                originalTitle = movieEntity.originalTitle,
                originalLanguage = movieEntity.originalLanguage,
                title = movieEntity.title,
                backdropPath = movieEntity.backdropPath,
                popularity = movieEntity.popularity,
                voteCount = movieEntity.voteCount,
                video = movieEntity.video,
                voteAverage = movieEntity.voteAverage,
                rating = movieEntity.rating
            )
        }

        fun fromMovie(movie: Movie): MovieEntity {
            return MovieEntity(
                posterPath = movie.posterPath,
                adult = movie.adult,
                overview = movie.overview,
                releaseDate = movie.releaseDate,
                genreIds = movie.genreIds,
                genres = movie.genres?.map { it.let(GenreEntity::fromGenre) },
                id = movie.id,
                originalTitle = movie.originalTitle,
                originalLanguage = movie.originalLanguage,
                title = movie.title,
                backdropPath = movie.backdropPath,
                popularity = movie.popularity,
                voteCount = movie.voteCount,
                video = movie.video,
                voteAverage = movie.voteAverage,
                rating = movie.rating
            )
        }
    }
}
