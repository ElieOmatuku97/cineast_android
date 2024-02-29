package elieomatuku.cineast_android.presentation.discover

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.movie.GetDiscoverContentFactory
import elieomatuku.cineast_android.domain.interactor.movie.GetGenres
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.interactor.user.GetAccessToken
import elieomatuku.cineast_android.domain.interactor.user.Logout
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.presentation.base.BaseViewModel
import elieomatuku.cineast_android.presentation.utils.SingleEvent
import elieomatuku.cineast_android.presentation.utils.ViewErrorController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-09-05
 */

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val getDiscoverContentFactory: GetDiscoverContentFactory,
    private val getGenres: GetGenres,
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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private fun getDiscoverContent() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result =
                runUseCase(getDiscoverContentFactory.obtainUseCase(), Unit)
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    discoverContents = result.data.content,
                    isLoggedIn = result.data.isLoggedIn,
                    viewError = null
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )

                else -> DiscoverViewState()
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

    fun logout() {
        viewModelScope.launch {
            runUseCase(logout, Unit)
            state = state.copy(isLoggedIn = false)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            getDiscoverContent()
            _isRefreshing.emit(false)
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