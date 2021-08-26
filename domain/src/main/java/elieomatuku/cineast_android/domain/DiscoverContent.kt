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
        const val TYPE_POPULAR_MOVIE = 1
        const val TYPE_PERSONALITIES = 2
        const val TYPE_NOW_PLAYING_MOVIE = 3
        const val TYPE_UPCOMING_MOVIE = 4
        const val TYPE_TOP_RATED_MOVIE = 5

        const val TIMESTAMP = "timestamp"
        const val STALE_MS = 3600000 // Data is stale after an 1hour

        fun emptyDiscoverContent(): DiscoverContent {
            return DiscoverContent()
        }

        fun isUpToDate(timeStamp: Long): Boolean {
            return System.currentTimeMillis() - timeStamp < STALE_MS
        }
    }

    fun getFilteredWidgets(): MutableMap<Int, Pair<Int, List<Content>>> {
        val filteredWidgets: MutableMap<Int, Pair<Int, List<Content>>> = mutableMapOf()

        filteredWidgets[getSummaryPosition(TYPE_POPULAR_MOVIE)] =
            Pair(R.string.popular_movies, popularMovies)
        filteredWidgets[getSummaryPosition(TYPE_PERSONALITIES)] =
            Pair(R.string.popular_people, people)
        filteredWidgets[getSummaryPosition(TYPE_NOW_PLAYING_MOVIE)] =
            Pair(R.string.now_playing, nowPlayingMovies)
        filteredWidgets[getSummaryPosition(TYPE_UPCOMING_MOVIE)] =
            Pair(R.string.upcoming, upcomingMovies)
        filteredWidgets[getSummaryPosition(TYPE_TOP_RATED_MOVIE)] =
            Pair(R.string.top_rated, topRatedMovies)

        return filteredWidgets
    }

    private fun getSummaryPosition(widgetPosition: Int): Int {
        return (widgetPosition - 1)
    }

    fun isEmpty(): Boolean {
        return popularMovies.isEmpty() && nowPlayingMovies.isEmpty() && upcomingMovies.isEmpty() && topRatedMovies.isEmpty() && people.isEmpty()
    }
}
