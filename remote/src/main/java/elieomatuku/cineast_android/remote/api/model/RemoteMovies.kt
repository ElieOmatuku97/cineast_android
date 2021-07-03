package elieomatuku.cineast_android.remote.api.model

import androidx.annotation.Keep

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
)
