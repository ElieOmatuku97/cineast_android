package elieomatuku.cineast_android.discover

import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.movie.GetDiscoverContent
import elieomatuku.cineast_android.domain.interactor.movie.GetGenres
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.interactor.user.GetAccessToken
import elieomatuku.cineast_android.domain.interactor.user.IsLoggedIn
import elieomatuku.cineast_android.domain.interactor.user.Logout
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.base.BaseViewModel
import elieomatuku.cineast_android.ui.utils.SingleEvent
import elieomatuku.cineast_android.ui.utils.ViewErrorController


/**
 * Created by elieomatuku on 2021-09-05
 */

class DiscoverViewModel(
    private val getDiscoverContent: GetDiscoverContent,
    private val getGenres: GetGenres,
    private val isLoggedIn: IsLoggedIn,
    private val logout: Logout,
    private val getAccessToken: GetAccessToken
) : BaseViewModel<DiscoverViewState>(
    DiscoverViewState(),
) {

    init {
        getDiscoverContent()
        getGenres()
    }

    val genres: List<Genre>?
        get() = state.genres

    fun getDiscoverContent() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result =
                runUseCase(getDiscoverContent, Unit)
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    discoverContents = result.data
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )
                else -> DiscoverViewState()
            }
        }
    }

    fun getIsLoggedIn() {
        viewModelScope.launch {
            state = when (val result = runUseCase(isLoggedIn, Unit)) {
                is Success -> {
                    state.copy(isLoggedIn = result.data)
                }

                is Fail -> {
                    state.copy(
                        viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    )
                }
            }
        }
    }

    fun logIn() {
        viewModelScope.launch {
            state = when (val result = runUseCase(getAccessToken, Unit)) {
                is Success -> {
                    state.copy(accessToken = SingleEvent(result.data))
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
        return state.isLoggedIn
    }

    fun logout() {
        viewModelScope.launch {
            runUseCase(logout, Unit)
            state = state.copy(isLoggedIn = false)
        }
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
                else -> DiscoverViewState()
            }
        }
    }
}