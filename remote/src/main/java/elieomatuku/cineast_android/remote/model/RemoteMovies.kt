package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.MovieEntity

@Keep
data class RemoteMovies(val results: List<RemoteMovie> = listOf())

@Keep
data class RemoteMovie(
    val poster_path: String? = null,
    val adult: Boolean = false,
    val overview: String? = null,
    val release_date: String? = null,
    val genre_ids: List<Int>? = listOf(),
    val genres: List<RemoteGenre>? = listOf(),
    val id: Int,
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
        fun toMovieEntity(remoteMovie: RemoteMovie): MovieEntity {
            return MovieEntity(
                remoteMovie.poster_path,
                remoteMovie.adult,
                remoteMovie.overview,
                remoteMovie.release_date,
                remoteMovie.genre_ids,
                remoteMovie.genres?.map { it.let(RemoteGenre::toGenreEntity) },
                remoteMovie.id,
                remoteMovie.original_title,
                remoteMovie.original_language,
                remoteMovie.title,
                remoteMovie.backdrop_path,
                remoteMovie.popularity,
                remoteMovie.vote_count,
                remoteMovie.video,
                remoteMovie.vote_average,
                remoteMovie.rating
            )
        }
    }
}
