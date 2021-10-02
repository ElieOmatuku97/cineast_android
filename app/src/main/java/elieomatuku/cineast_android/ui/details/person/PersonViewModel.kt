package elieomatuku.cineast_android.ui.details.person

import androidx.lifecycle.viewModelScope
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.people.GetImages
import elieomatuku.cineast_android.domain.interactor.people.GetPersonDetails
import elieomatuku.cineast_android.domain.interactor.people.GetKnownForMovies
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.ui.base.BaseViewModel
import elieomatuku.cineast_android.utils.SingleEvent
import elieomatuku.cineast_android.utils.ViewErrorController
import kotlinx.coroutines.launch


/**
 * Created by elieomatuku on 2021-10-02
 */

class PersonViewModel(
    private val getPersonDetails: GetPersonDetails,
    private val getKnownForMovies: GetKnownForMovies,
    private val getImages: GetImages
) : BaseViewModel<PersonViewState>(PersonViewState()) {

    val person
        get() = state.person

    val knownForMovies
        get() = state.knownFor

    val posters
        get() = state.posters

    fun getPersonDetails(person: Person) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = runUseCase(getPersonDetails, GetPersonDetails.Input(person))
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

    fun getKnownForMovies(person: Person) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = runUseCase(getKnownForMovies, GetKnownForMovies.Input(person))
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

    fun getImages(person: Person) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = runUseCase(getImages, GetImages.Input(person))
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