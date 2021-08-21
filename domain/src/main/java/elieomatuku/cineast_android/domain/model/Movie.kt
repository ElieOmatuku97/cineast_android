package elieomatuku.cineast_android.domain.model

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val poster_path: String? = null,
    val adult: Boolean = false,
    val overview: String? = null,
    val releaseDate: String? = null,
    val genreIds: List<Int>? = listOf(),
    val genres: List<Genre>? = listOf(),
    val id: Int,
    val originalTitle: String? = null,
    val originalLanguage: String? = null,
    val title: String? = null,
    val backdropPath: String? = null,
    val popularity: Double? = null,
    val vote_count: Int? = null,
    val video: Boolean? = true,
    val vote_average: Float? = null,
    val rating: Float? = null
) : Content()
