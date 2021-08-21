package elieomatuku.cineast_android.domain.model


/**
 * Created by elieomatuku on 2021-08-21
 */

data class DiscoverContent(
    val popularMovies: List<Movie> = listOf(),
    val nowPlayingMovies: List<Movie> = listOf(),
    val upcomingMovies: List<Movie> = listOf(),
    val topRatedMovies: List<Movie> = listOf(),
    val personalities: List<Personality> = listOf()
)
