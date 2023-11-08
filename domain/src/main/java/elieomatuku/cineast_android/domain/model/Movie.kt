package elieomatuku.cineast_android.domain.model

import java.io.Serializable

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
    val currentUserRating: Float? = null,
    val watchListState: WatchListState = WatchListState(),
    val favoritesState: FavoriteState = FavoriteState(),
    val posters: List<Image>? = listOf(),
    val movieSummary: MovieSummary? = null,
) : Content {
    override val imagePath: String?
        get() = posterPath
    override val subTitle: String?
        get() = releaseDate
}

data class WatchListState(
    val isVisible: Boolean = false,
    val isSelected: Boolean = false
): Serializable

data class FavoriteState(
    val isVisible: Boolean = false,
    val isSelected: Boolean = false
): Serializable