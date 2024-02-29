package elieomatuku.cineast_android.presentation.details.person

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.people.GetImages
import elieomatuku.cineast_android.domain.interactor.people.GetPersonDetails
import elieomatuku.cineast_android.domain.interactor.people.GetKnownForMovies
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.presentation.base.BaseViewModel
import elieomatuku.cineast_android.presentation.utils.SingleEvent
import elieomatuku.cineast_android.presentation.utils.ViewErrorController
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by elieomatuku on 2021-10-02
 */

private const val PERSON_ID = "personId"

@HiltViewModel
class PersonViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPersonDetails: GetPersonDetails,
    private val getKnownForMovies: GetKnownForMovies,
    private val getImages: GetImages
) : BaseViewModel<PersonViewState>(PersonViewState()) {

    init {
        val personId: Int = checkNotNull(savedStateHandle[PERSON_ID])
        getPersonDetails(personId)
        getKnownForMovies(personId)
        getImages(personId)
    }

    val person
        get() = state.person

    val knownForMovies
        get() = state.knownFor

    val posters
        get() = state.posters

    private fun getPersonDetails(personId: Int) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = runUseCase(getPersonDetails, GetPersonDetails.Input(personId))
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    person = result.data
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )

                else -> PersonViewState()
            }
        }

    }

    private fun getKnownForMovies(personId: Int) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = runUseCase(getKnownForMovies, GetKnownForMovies.Input(personId))
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    knownFor = result.data,
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )

                else -> PersonViewState()
            }
        }

    }

    private fun getImages(personId: Int) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = runUseCase(getImages, GetImages.Input(personId))
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    posters = result.data.peoplePosters,
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )

                else -> PersonViewState()
            }
        }

    }
}