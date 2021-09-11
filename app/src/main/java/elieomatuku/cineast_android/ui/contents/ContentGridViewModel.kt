package elieomatuku.cineast_android.ui.contents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.service.ContentService
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.movie.GetGenres
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.ui.base.BaseViewModel
import elieomatuku.cineast_android.utils.SingleEvent
import elieomatuku.cineast_android.utils.ViewErrorController
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

/**
 * Created by elieomatuku on 2021-06-05
 */

abstract class ContentGridViewModel(private val getGenres: GetGenres) :
    BaseViewModel<ContentGridViewState>(ContentGridViewState()) {

    protected val contentService: ContentService by App.getKodein.instance()
    val contentLiveData: MutableLiveData<List<Content>> = MutableLiveData()
    val errorMsgLiveData: MutableLiveData<String> = MutableLiveData()

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
