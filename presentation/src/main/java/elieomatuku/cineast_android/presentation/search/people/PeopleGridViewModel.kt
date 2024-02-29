package elieomatuku.cineast_android.presentation.search.people

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.movie.GetGenres
import elieomatuku.cineast_android.domain.interactor.people.GetPersonalities
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.presentation.contents.ContentGridViewState
import elieomatuku.cineast_android.presentation.contents.ContentGridViewModel
import elieomatuku.cineast_android.presentation.utils.SingleEvent
import elieomatuku.cineast_android.presentation.utils.ViewErrorController
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-06-05
 */

@HiltViewModel
class PeopleGridViewModel @Inject constructor(
    private val getPersonalities: GetPersonalities,
    getGenres: GetGenres
) : ContentGridViewModel(getGenres) {
    init {
        getContent()
    }

    override fun getContent() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result =
                runUseCase(getPersonalities, Unit)
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    contents = result.data
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
