package elieomatuku.cineast_android.details.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import elieomatuku.cineast_android.base.BaseViewModel
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.movie.AddMovieToFavorites
import elieomatuku.cineast_android.domain.interactor.movie.AddMovieToWatchList
import elieomatuku.cineast_android.domain.interactor.movie.BaseGetMovie
import elieomatuku.cineast_android.domain.interactor.movie.GetMovieFactory
import elieomatuku.cineast_android.domain.interactor.movie.RemoveMovieFromFavorites
import elieomatuku.cineast_android.domain.interactor.movie.RemoveMovieFromWatchList
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.model.Image
import elieomatuku.cineast_android.utils.SingleEvent
import elieomatuku.cineast_android.utils.ViewErrorController
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-07-03
 */

private const val MOVIE_ID = "movieId"
private const val SCREEN_NAME = "screen_name"

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMovieFactory: GetMovieFactory,
    private val addMovieToWatchList: AddMovieToWatchList,
    private val addMovieToFavorites: AddMovieToFavorites,
    private val removeMovieFromFavorites: RemoveMovieFromFavorites,
    private val removeMovieFromWatchList: RemoveMovieFromWatchList,
) : BaseViewModel<MovieViewState>(MovieViewState()) {

    init {
        getMovieDetails()
    }

    private fun getMovieDetails() {
        val screenName: String? = savedStateHandle[SCREEN_NAME]
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val getMovie = getMovieFactory.obtainGetMovieUseCase()
            val movieId: Int? = savedStateHandle[MOVIE_ID]
            movieId?.let {
                val result = runUseCase(getMovie, BaseGetMovie.Input(movieId))
                state = when (result) {
                    is Success -> {
                        state.copy(
                            isLoading = false,
                            movie = result.data,
                            screenName = screenName,
                            viewError = null
                        )
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

    fun removeMovieFromWatchList() {
        state.movie?.let {
            viewModelScope.launch {
                runUseCase(removeMovieFromWatchList, RemoveMovieFromWatchList.Input(it))
            }
        }
    }

    fun addMovieToFavoriteList() {
        state.movie?.let {
            viewModelScope.launch {
                runUseCase(addMovieToFavorites, AddMovieToFavorites.Input(it))
            }
        }
    }

    fun addMovieToWatchList() {
        state.movie?.let {
            viewModelScope.launch {
                runUseCase(addMovieToWatchList, AddMovieToWatchList.Input(it))
            }
        }
    }

    fun removeMovieFromFavoriteList() {
        state.movie?.let {
            viewModelScope.launch {
                runUseCase(removeMovieFromFavorites, RemoveMovieFromFavorites.Input(it))
            }
        }
    }

    fun posters(): List<Image> {
        return state.movie?.posters ?: emptyList()
    }
}
