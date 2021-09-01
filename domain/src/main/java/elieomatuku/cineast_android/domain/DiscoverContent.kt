package elieomatuku.cineast_android.domain

import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person

class DiscoverContent(
    var popularMovies: List<Movie> = listOf(),
    var nowPlayingMovies: List<Movie> = listOf(),
    var upcomingMovies: List<Movie> = listOf(),
    var topRatedMovies: List<Movie> = listOf(),
    var people: List<Person> = listOf()
) {

    companion object {
        const val TIMESTAMP = "timestamp"
        private const val STALE_MS = 3600000 // Data is stale after an 1hour

        fun emptyDiscoverContent(): DiscoverContent {
            return DiscoverContent()
        }

        fun isUpToDate(timeStamp: Long): Boolean {
            return System.currentTimeMillis() - timeStamp < STALE_MS
        }
    }

    fun isEmpty(): Boolean {
        return popularMovies.isEmpty() && nowPlayingMovies.isEmpty() && upcomingMovies.isEmpty() && topRatedMovies.isEmpty() && people.isEmpty()
    }
}
