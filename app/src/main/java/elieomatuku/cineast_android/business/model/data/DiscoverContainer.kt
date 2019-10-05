package elieomatuku.cineast_android.business.model.data


import android.os.Parcelable
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.DiscoverAdapter
import kotlinx.android.parcel.Parcelize

@Parcelize
class DiscoverContainer(var popularMovies: List<Movie>? = listOf(),
                        var nowPlayingMovies: List<Movie>? = listOf(),
                        var upcomingMovies: List<Movie>? = listOf(),
                        var topRatedMovies: List<Movie>? = listOf(),
                        var popularPeople: List<Personality>? = listOf()) : Parcelable {


    fun getFilteredWidgets(): MutableMap<Int, Pair< Int, List<Widget>?>> {
        val filteredWidgets: MutableMap<Int, Pair< Int, List<Widget>?>> = mutableMapOf()

        filteredWidgets[getSummaryPosition(DiscoverAdapter.TYPE_POPULAR_MOVIE)] = Pair(R.string.popular_movies, popularMovies)
        filteredWidgets[getSummaryPosition(DiscoverAdapter.TYPE_POPULAR_PEOPLE)] = Pair(R.string.popular_people,popularPeople)
        filteredWidgets[getSummaryPosition(DiscoverAdapter.TYPE_NOW_PLAYING_MOVIE)] = Pair(R.string.now_playing, nowPlayingMovies)
        filteredWidgets[getSummaryPosition(DiscoverAdapter.TYPE_UPCOMING_MOVIE)] = Pair(R.string.upcoming, upcomingMovies)
        filteredWidgets[getSummaryPosition(DiscoverAdapter.TYPE_TOP_RATED_MOVIE)] = Pair(R.string.top_rated, topRatedMovies)

        return filteredWidgets
    }

    private fun getSummaryPosition(widgetPosition: Int): Int {
        return (widgetPosition - 1)
    }
}