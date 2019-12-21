package elieomatuku.cineast_android.model


import android.os.Parcelable
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.model.data.Movie
import elieomatuku.cineast_android.model.data.Personality
import elieomatuku.cineast_android.model.data.Content
import kotlinx.android.parcel.Parcelize

@Parcelize
class DiscoverContent(
        var popularMovies: List<Movie> = listOf(),
        var nowPlayingMovies: List<Movie> = listOf(),
        var upcomingMovies: List<Movie> = listOf(),
        var topRatedMovies: List<Movie> = listOf(),
        var popularPeople: List<Personality> = listOf()) : Parcelable {


    companion object {
        const val TYPE_POPULAR_MOVIE = 1
        const val TYPE_POPULAR_PEOPLE = 2
        const val TYPE_NOW_PLAYING_MOVIE = 3
        const val TYPE_UPCOMING_MOVIE = 4
        const val TYPE_TOP_RATED_MOVIE = 5
    }


    fun getFilteredWidgets(): MutableMap<Int, Pair<Int, List<Content>?>> {
        val filteredWidgets: MutableMap<Int, Pair<Int, List<Content>?>> = mutableMapOf()

        filteredWidgets[getSummaryPosition(TYPE_POPULAR_MOVIE)] = Pair(R.string.popular_movies, popularMovies)
        filteredWidgets[getSummaryPosition(TYPE_POPULAR_PEOPLE)] = Pair(R.string.popular_people, popularPeople)
        filteredWidgets[getSummaryPosition(TYPE_NOW_PLAYING_MOVIE)] = Pair(R.string.now_playing, nowPlayingMovies)
        filteredWidgets[getSummaryPosition(TYPE_UPCOMING_MOVIE)] = Pair(R.string.upcoming, upcomingMovies)
        filteredWidgets[getSummaryPosition(TYPE_TOP_RATED_MOVIE)] = Pair(R.string.top_rated, topRatedMovies)

        return filteredWidgets
    }

    private fun getSummaryPosition(widgetPosition: Int): Int {
        return (widgetPosition - 1)
    }

    fun hasMissingContent(): Boolean {
        return popularMovies.isEmpty()|| nowPlayingMovies.isEmpty() || upcomingMovies.isEmpty() || topRatedMovies.isEmpty() || popularPeople.isEmpty()
    }
}