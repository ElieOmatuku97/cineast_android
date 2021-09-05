package elieomatuku.cineast_android.domain.model

/**
 * Created by elieomatuku on 2021-08-21
 */

data class DiscoverContents(
    val popularMovies: List<Content> = listOf(),
    val nowPlayingMovies: List<Content> = listOf(),
    val upcomingMovies: List<Content> = listOf(),
    val topRatedMovies: List<Content> = listOf(),
    val people: List<Content> = listOf()
)
