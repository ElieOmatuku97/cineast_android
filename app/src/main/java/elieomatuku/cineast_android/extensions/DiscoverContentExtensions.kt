package elieomatuku.cineast_android.extensions

import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.DiscoverContent

/**
 * Created by elieomatuku on 2021-08-26
 */

enum class Content(val position: Int) {
    PopularMovie(1),
    PopularPeople(2),
    NowPlayingMovie(3),
    UpcomingMovie(4),
    TopRatedMovie(5)
}

fun DiscoverContent.isEmpty(): Boolean {
    return popularMovies.isEmpty() && nowPlayingMovies.isEmpty() && upcomingMovies.isEmpty() && topRatedMovies.isEmpty() && people.isEmpty()
}

fun DiscoverContent.emptyDiscoverContent(): DiscoverContent {
    return DiscoverContent()
}

fun DiscoverContent.getFilteredWidgets(): MutableMap<Int, Pair<Int, List<*>>> {
    val filteredWidgets: MutableMap<Int, Pair<Int, List<*>>> = mutableMapOf()

    filteredWidgets[getContentPosition(Content.PopularMovie)] =
        Pair(R.string.popular_movies, popularMovies)
    filteredWidgets[getContentPosition(Content.PopularPeople)] =
        Pair(R.string.popular_people, people)
    filteredWidgets[getContentPosition(Content.NowPlayingMovie)] =
        Pair(R.string.now_playing, nowPlayingMovies)
    filteredWidgets[getContentPosition(Content.UpcomingMovie)] =
        Pair(R.string.upcoming, upcomingMovies)
    filteredWidgets[getContentPosition(Content.TopRatedMovie)] =
        Pair(R.string.top_rated, topRatedMovies)

    return filteredWidgets
}

fun DiscoverContent.getContentPosition(content: Content): Int {
    return (content.position - 1)
}
