package elieomatuku.cineast_android.ui.settings.user_movies

import androidx.lifecycle.viewModelScope
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.movie.*
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.ui.base.BaseViewModel
import elieomatuku.cineast_android.ui.utils.SingleEvent
import elieomatuku.cineast_android.ui.utils.ViewErrorController
import kotlinx.coroutines.launch

/**
 * Created by elieomatuku on 2021-05-29
 */

class UserMoviesViewModel(
    private val getGenres: GetGenres,
    private val getFavorites: GetFavorites,
    private val getWatchList: GetWatchList,
    private val getUserRatedMovies: GetUserRatedMovies,
    private val removeMovieFromWatchList: RemoveMovieFromWatchList,
    private val removeMovieFromFavorites: RemoveMovieFromFavorites
) : BaseViewModel<UserMoviesViewState>(UserMoviesViewState()) {

    val genres: List<Genre>
        get() = state.genres

    init {
        getGenres()
    }

    private fun getGenres() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result =
                runUseCase(getGenres, Unit)
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    genres = result.data
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )
                else -> UserMoviesViewState()
            }
        }

    }

    fun getFavourites() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result =
                runUseCase(getFavorites, Unit)
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    userMovies = result.data
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )
                else -> UserMoviesViewState()
            }
        }
    }

    fun getWatchList() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result =
                runUseCase(getWatchList, Unit)
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    userMovies = result.data
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )
                else -> UserMoviesViewState()
            }
        }
    }

    fun getRatedMovies() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result =
                runUseCase(getUserRatedMovies, Unit)
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    userMovies = result.data
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )
                else -> UserMoviesViewState()
            }
        }
    }

    fun removeMovieFromWatchList(movie: Movie) {
        viewModelScope.launch {
            runUseCase(removeMovieFromWatchList, RemoveMovieFromWatchList.Input(movie))
        }
    }

    fun removeMovieFromFavorites(movie: Movie) {
        viewModelScope.launch {
            runUseCase(removeMovieFromFavorites, RemoveMovieFromFavorites.Input(movie))
        }
    }
}
