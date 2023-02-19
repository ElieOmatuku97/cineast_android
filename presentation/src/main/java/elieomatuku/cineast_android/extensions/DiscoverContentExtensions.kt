package elieomatuku.cineast_android.extensions

import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.DiscoverContents

/**
 * Created by elieomatuku on 2021-08-26
 */

sealed class DiscoverWidget {
    data class Header(
        val value: List<Content>
    ) : DiscoverWidget()

    data class Movies(
        val titleResources: Int,
        val value: List<Content>
    ) : DiscoverWidget()

    data class People(
        val titleResources: Int,
        val value: List<Content>
    ) : DiscoverWidget()

    object Login : DiscoverWidget()
}

fun DiscoverContents.isEmpty(): Boolean {
    return popularMovies.isEmpty() && nowPlayingMovies.isEmpty() && upcomingMovies.isEmpty() && topRatedMovies.isEmpty() && people.isEmpty()
}

fun DiscoverContents.getWidgets(): List<DiscoverWidget> {
    return buildList {
        add(DiscoverWidget.Header(value = popularMovies))
        add(DiscoverWidget.Movies(titleResources = R.string.popular_movies, value = popularMovies))
        add(DiscoverWidget.People(titleResources = R.string.popular_people, value = people))
        add(DiscoverWidget.Movies(titleResources = R.string.now_playing, value = nowPlayingMovies))
        add(DiscoverWidget.Movies(titleResources = R.string.upcoming, value = upcomingMovies))
        add(DiscoverWidget.Movies(titleResources = R.string.top_rated, value = topRatedMovies))
        add(DiscoverWidget.Login)
    }
}