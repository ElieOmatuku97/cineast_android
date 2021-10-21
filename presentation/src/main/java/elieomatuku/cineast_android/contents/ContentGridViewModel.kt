package elieomatuku.cineast_android.contents

import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.movie.GetGenres
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.base.BaseViewModel
import elieomatuku.cineast_android.ui.utils.SingleEvent
import elieomatuku.cineast_android.ui.utils.ViewErrorController

/**
 * Created by elieomatuku on 2021-06-05
 */

abstract class ContentGridViewModel(private val getGenres: GetGenres) :
    BaseViewModel<ContentGridViewState>(ContentGridViewState()) {

    abstract fun getContent()

    fun getGenres() {
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
                else -> ContentGridViewState()
            }
        }
    }

}
