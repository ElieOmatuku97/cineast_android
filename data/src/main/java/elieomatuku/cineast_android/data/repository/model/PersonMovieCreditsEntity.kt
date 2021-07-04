package elieomatuku.cineast_android.data.repository.model

data class PersonMovieCreditsEntity(
    val movies: List<KnownFor> = listOf()
)

data class KnownFor(
    val releaseDate: String?,
    val title: String?,
    val originalTitle: String?,
    val id: Int?,
    val backdropPath: String?,
    val posterPath: String?
)
