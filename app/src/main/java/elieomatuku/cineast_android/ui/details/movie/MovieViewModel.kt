package elieomatuku.cineast_android.ui.details.movie

import androidx.lifecycle.viewModelScope
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.business.client.TmdbUserClient
import elieomatuku.cineast_android.business.service.ContentService
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.domain.model.Poster
import elieomatuku.cineast_android.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

/**
 * Created by elieomatuku on 2021-07-03
 */

class MovieViewModel : BaseViewModel<MovieViewState>(MovieViewState()) {

    val contentService: ContentService by App.kodein.instance()
    private val tmdbUserClient: TmdbUserClient by App.kodein.instance()
    private val tmdbContentClient: TmdbContentClient by App.kodein.instance()

    fun getMovieDetails(movie: Movie, screenName: String?, genres: List<Genre>?) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val trailers = contentService.getMovieVideos(movie)?.results
            val movieFacts = contentService.getMovieDetails(movie)
            val movieCredits = contentService.getMovieCredits(movie)
            val cast = movieCredits?.cast
            val crew = movieCredits?.crew
            val response = contentService.getSimilarMovie(movie)
            val similarMovies = response?.results

            val movieSummary = MovieSummary(
                movie,
                trailers,
                movieFacts,
                genres,
                screenName,
                cast,
                crew,
                similarMovies
            )

            val imageResponse = contentService.getMovieImages(movie.id)
            state = state.copy(
                movieSummary = movieSummary,
                posters = imageResponse.getOrNull()?.posters,
                isLoading = false
            )

            if (tmdbUserClient.isLoggedIn()) {
                val watchListResponse = contentService.getWatchList()

                if (watchListResponse.isSuccess) {

                    val movies = watchListResponse.getOrNull()?.results

                    val isInWatchList = movies?.let {
                        it.contains(movieSummary.movie)
                    } ?: false

                    val favouritesResponse = contentService.getFavoriteList()
                    if (favouritesResponse.isSuccess) {
                        val isInFavorites = favouritesResponse.getOrNull()?.results?.let {
                            it.contains(movieSummary.movie)
                        } ?: false

                        state = state.copy(
                            isInFavorites = isInFavorites,
                            isInWatchList = isInWatchList
                        )
                    }
                }
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return tmdbUserClient.isLoggedIn()
    }

    fun removeMovieFromWatchList() {
        state.movieSummary?.movie?.let {
            tmdbContentClient.removeMovieFromWatchList(it)
        }
    }

    fun addMovieToFavoriteList() {
        state.movieSummary?.movie?.let {
            tmdbContentClient.addMovieToFavoriteList(it)
        }
    }

    fun addMovieToWatchList() {
        state.movieSummary?.movie?.let {
            tmdbContentClient.addMovieToWatchList(it)
        }
    }

    fun removeMovieFromFavoriteList() {
        state.movieSummary?.movie?.let {
            tmdbContentClient.removeMovieFromFavoriteList(it)
        }
    }

    fun posters(): List<Poster> {
        return state.posters ?: emptyList()
    }
}
