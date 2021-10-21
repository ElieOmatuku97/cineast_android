package elieomatuku.cineast_android.search

import androidx.lifecycle.viewModelScope
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.movie.SearchMovies
import elieomatuku.cineast_android.domain.interactor.people.SearchPeople
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.base.BaseViewModel
import elieomatuku.cineast_android.utils.SingleEvent
import elieomatuku.cineast_android.utils.ViewErrorController
import kotlinx.coroutines.launch

/**
 * Created by elieomatuku on 2021-10-02
 */

class SearchViewModel(
    private val searchPeople: SearchPeople,
    private val searchMovies: SearchMovies
) : BaseViewModel<SearchViewState>(
    SearchViewState(),
) {

    fun searchMovies(argQuery: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result =
                runUseCase(searchMovies, SearchMovies.Input(argQuery))
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    results = result.data
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )
                else -> SearchViewState()
            }
        }
    }

    fun searchPeople(argQuery: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result =
                runUseCase(searchPeople, SearchPeople.Input(argQuery))
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    results = result.data
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )
                else -> SearchViewState()
            }
        }
    }
}