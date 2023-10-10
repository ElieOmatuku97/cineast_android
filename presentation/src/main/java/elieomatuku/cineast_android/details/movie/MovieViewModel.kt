package elieomatuku.cineast_android.details.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.movie.*
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.interactor.user.IsLoggedIn
import elieomatuku.cineast_android.domain.model.Image
import elieomatuku.cineast_android.base.BaseViewModel
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.utils.SingleEvent
import elieomatuku.cineast_android.utils.ViewErrorController
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-07-03
 */

private const val MOVIE_ID = "movieId"
private const val SCREEN_NAME = "screen_name"

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMovie: GetMovie,
    private val getMovieSummary: GetMovieSummary,
    private val isLoggedIn: IsLoggedIn,
    private val getWatchList: GetWatchList,
    private val getFavorites: GetFavorites,
    private val addMovieToWatchList: AddMovieToWatchList,
    private val addMovieToFavorites: AddMovieToFavorites,
    private val removeMovieFromFavorites: RemoveMovieFromFavorites,
    private val removeMovieFromWatchList: RemoveMovieFromWatchList,
) : BaseViewModel<MovieViewState>(MovieViewState()) {

    init {
        getMovieDetails()
        getIsLoggedIn()
        getFavorites()
        getWatchLists()
    }

    private fun getMovieDetails() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val movieId: Int? = savedStateHandle[MOVIE_ID]
            movieId?.let {
                val result = runUseCase(getMovie, GetMovie.Input(movieId))
                state = when (result) {
                    is Success -> {
                        val state = getMovieSummary(result.data)
                        state
                    }

                    is Fail -> state.copy(
                        viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                        isLoading = false
                    )

                    else -> MovieViewState()
                }
            }
        }
    }

    private suspend fun getMovieSummary(movie: Movie): MovieViewState {
        val screenName: String? = savedStateHandle[SCREEN_NAME]
        return withContext(viewModelScope.coroutineContext) {
            val state = when (val result =
                runUseCase(getMovieSummary, GetMovieSummary.Input(movie))) {
                is Success -> state.copy(
                    isLoading = false,
                    movieSummary = result.data,
                    posters = result.data.posters,
                    screenName = screenName,
                    viewError = null
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(
                        ViewErrorController.mapThrowable(
                            result.throwable
                        )
                    ),
                    isLoading = false
                )

                else -> MovieViewState()
            }
            state
        }
    }

    private fun getIsLoggedIn() {
        viewModelScope.launch {
            state = when (val result = runUseCase(isLoggedIn, Unit)) {
                is Success -> {
                    state.copy(
                        isLoggedIn = SingleEvent(result.data),
                        viewError = null
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
                    state.copy(isInFavorites = isInFavorites, viewError = null)
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
                    state.copy(isInFavorites = isInWatchList, viewError = null)
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
