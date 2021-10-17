package elieomatuku.cineast_android.ui.details

import androidx.lifecycle.viewModelScope
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.movie.GetGenres
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.ui.base.BaseViewModel
import elieomatuku.cineast_android.ui.utils.SingleEvent
import elieomatuku.cineast_android.ui.utils.ViewErrorController
import kotlinx.coroutines.launch


/**
 * Created by elieomatuku on 2021-05-06
 */

class MoviesViewModel(private val getGenres: GetGenres) :
    BaseViewModel<MoviesViewState>(MoviesViewState()) {
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
                else -> MoviesViewState()
            }
        }
    }
}

