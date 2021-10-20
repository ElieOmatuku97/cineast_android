package elieomatuku.cineast_android.ui.details.movie

import androidx.lifecycle.viewModelScope
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.movie.*
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.interactor.user.IsLoggedIn
import elieomatuku.cineast_android.domain.model.Image
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.ui.base.BaseViewModel
import elieomatuku.cineast_android.ui.utils.SingleEvent
import elieomatuku.cineast_android.ui.utils.ViewErrorController
import kotlinx.coroutines.launch


/**
 * Created by elieomatuku on 2021-07-03
 */

class MovieViewModel(
    private val getMovieSummary: GetMovieSummary,
    private val isLoggedIn: IsLoggedIn,
    private val getWatchList: GetWatchList,
    private val getFavorites: GetFavorites,
    private val addMovieToWatchList: AddMovieToWatchList,
    private val addMovieToFavorites: AddMovieToFavorites,
    private val removeMovieFromFavorites: RemoveMovieFromFavorites,
    private val removeMovieFromWatchList: RemoveMovieFromWatchList
) : BaseViewModel<MovieViewState>(MovieViewState()) {

    init {
        getIsLoggedIn()
        getFavorites()
        getWatchLists()
    }

    fun getMovieDetails(movie: Movie, screenName: String?) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = runUseCase(getMovieSummary, GetMovieSummary.Input(movie))
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    movieSummary = result.data,
                    posters = result.data.posters,
                    screenName = screenName
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )
                else -> MovieViewState()
            }
        }
    }

    private fun getIsLoggedIn() {
        viewModelScope.launch {
            state = when (val result = runUseCase(isLoggedIn, Unit)) {
                is Success -> {
                    state.copy(
                        isLoggedIn = SingleEvent(result.data)
                    )
                }

                is Fail -> {
                    state.copy(
                        viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    )
                }
            }
        }
    }

    fun getFavorites() {
        viewModelScope.launch {
            state = when (val result = runUseCase(getFavorites, Unit)) {
                is Success -> {
                    val movies = result.data
                    val isInFavorites = movies.contains(state.movieSummary?.movie)
                    state.copy(isInFavorites = isInFavorites)
                }

                is Fail -> {
                    state.copy(
                        viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    )
                }
            }
        }
    }

    fun getWatchLists() {
        viewModelScope.launch {
            state = when (val result = runUseCase(getWatchList, Unit)) {
                is Success -> {
                    val movies = result.data
                    val isInWatchList = movies.contains(state.movieSummary?.movie)
                    state.copy(isInFavorites = isInWatchList)
                }

                is Fail -> {
                    state.copy(
                        viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    )
                }
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return state.isLoggedIn?.peek() ?: false
    }

    fun removeMovieFromWatchList() {
        state.movieSummary?.movie?.let {
            viewModelScope.launch {
                runUseCase(removeMovieFromWatchList, RemoveMovieFromWatchList.Input(it))
            }
        }
    }

    fun addMovieToFavoriteList() {
        state.movieSummary?.movie?.let {
            viewModelScope.launch {
                runUseCase(addMovieToFavorites, AddMovieToFavorites.Input(it))
            }
        }
    }

    fun addMovieToWatchList() {
        state.movieSummary?.movie?.let {
            viewModelScope.launch {
                runUseCase(addMovieToWatchList, AddMovieToWatchList.Input(it))
            }
        }
    }

    fun removeMovieFromFavoriteList() {
        state.movieSummary?.movie?.let {
            viewModelScope.launch {
                runUseCase(removeMovieFromFavorites, RemoveMovieFromFavorites.Input(it))
            }
        }
    }

    fun posters(): List<Image> {
        return state.posters ?: emptyList()
    }
}
