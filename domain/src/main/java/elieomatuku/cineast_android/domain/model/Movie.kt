package elieomatuku.cineast_android.domain.model

data class Movie(
    val posterPath: String? = null,
    val adult: Boolean = false,
    val overview: String? = null,
    val releaseDate: String? = null,
    val genreIds: List<Int>? = listOf(),
    val genres: List<Genre>? = listOf(),
    override val id: Int,
    override val name: String? = null,
    val originalTitle: String? = null,
    val originalLanguage: String? = null,
    override val title: String? = null,
    val backdropPath: String? = null,
    val popularity: Double? = null,
    val voteCount: Int? = null,
    val video: Boolean? = true,
    val voteAverage: Float? = null,
    val currentUserRating: Float? = null
) : Content {
    override val imagePath: String?
        get() = posterPath
    override val subTitle: String?
        get() = releaseDate
}
