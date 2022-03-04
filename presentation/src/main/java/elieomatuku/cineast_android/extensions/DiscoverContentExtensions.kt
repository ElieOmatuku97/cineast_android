package elieomatuku.cineast_android.extensions

import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.DiscoverContents

/**
 * Created by elieomatuku on 2021-08-26
 */

enum class Contents(val position: Int, val titleResources: Int, var value: List<Content>?) {
    PopularMovies(1, R.string.popular_movies, null),
    PopularPeoples(2, R.string.popular_people, null),
    NowPlayingMovies(3, R.string.now_playing, null),
    UpcomingMovies(4, R.string.upcoming, null),
    TopRatedMovies(5, R.string.top_rated, null)
}

fun DiscoverContents.isEmpty(): Boolean {
    return popularMovies.isEmpty() && nowPlayingMovies.isEmpty() && upcomingMovies.isEmpty() && topRatedMovies.isEmpty() && people.isEmpty()
}

fun DiscoverContents.getFilteredWidgets(): MutableMap<Int, Contents> {
    val filteredWidgets: MutableMap<Int, Contents> = mutableMapOf()

    Contents.PopularMovies.value = popularMovies
    Contents.PopularPeoples.value = people
    Contents.NowPlayingMovies.value = nowPlayingMovies
    Contents.UpcomingMovies.value = upcomingMovies
    Contents.TopRatedMovies.value = topRatedMovies

    filteredWidgets[getContentPosition(Contents.PopularMovies)] = Contents.PopularMovies
    filteredWidgets[getContentPosition(Contents.PopularPeoples)] =
        Contents.PopularPeoples
    filteredWidgets[getContentPosition(Contents.NowPlayingMovies)] =
        Contents.NowPlayingMovies
    filteredWidgets[getContentPosition(Contents.UpcomingMovies)] =
        Contents.UpcomingMovies
    filteredWidgets[getContentPosition(Contents.TopRatedMovies)] =
        Contents.TopRatedMovies

    return filteredWidgets
}

fun DiscoverContents.getContentPosition(contents: Contents): Int {
    return (contents.position - 1)
}
