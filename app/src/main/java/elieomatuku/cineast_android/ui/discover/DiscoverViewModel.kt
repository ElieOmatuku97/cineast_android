package elieomatuku.cineast_android.ui.discover

import androidx.lifecycle.viewModelScope
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.movie.GetDiscoverContent
import elieomatuku.cineast_android.domain.interactor.movie.GetGenres
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.ui.base.BaseViewModel
import elieomatuku.cineast_android.utils.SingleEvent
import elieomatuku.cineast_android.utils.ViewErrorController
import kotlinx.coroutines.launch


/**
 * Created by elieomatuku on 2021-09-05
 */

class DiscoverViewModel(
    private val getDiscoverContent: GetDiscoverContent,
    private val getGenres: GetGenres
) : BaseViewModel<DiscoverViewState>(
    DiscoverViewState()
) {

    init {
        getDiscoverContent()
    }

    private fun getDiscoverContent() {
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
}