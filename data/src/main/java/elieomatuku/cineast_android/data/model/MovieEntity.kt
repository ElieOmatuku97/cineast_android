package elieomatuku.cineast_android.data.model


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
)
