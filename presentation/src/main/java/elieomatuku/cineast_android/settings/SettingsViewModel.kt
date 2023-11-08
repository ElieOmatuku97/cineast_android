package elieomatuku.cineast_android.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.interactor.user.*
import elieomatuku.cineast_android.base.BaseViewModel
import elieomatuku.cineast_android.utils.SingleEvent
import elieomatuku.cineast_android.utils.ViewErrorController
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by elieomatuku on 2021-10-17
 */

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val isLoggedInUseCase: IsLoggedIn,
    private val logout: Logout,
    private val getSession: GetSession,
    private val getAccessToken: GetAccessToken,
    private val getAccount: GetAccount
) : BaseViewModel<SettingsViewState>(SettingsViewState()) {

    init {
        isLoggedIn()
    }

    val isLoggedIn: Boolean
        get() = state.isLoggedIn

    fun getAccessToken() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result =
                runUseCase(getAccessToken, Unit)
            state = when (result) {
                is Success -> state.copy(
                    accessToken = SingleEvent(result.data),
                    requestToken = SingleEvent(result.data.requestToken),
                    isLoading = false,
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )

                else -> SettingsViewState()
            }
        }
    }

    fun getSession() {
        state.requestToken?.consume()?.let { requestToken ->
            viewModelScope.launch {
                state = state.copy(isLoading = true)
                val result =
                    runUseCase(getSession, GetSession.Input(requestToken))
                state = when (result) {
                    is Success -> state.copy(
                        isLoading = false,
                        session = result.data,
                        isLoggedIn = result.data.sessionId != null
                    )

                    is Fail -> state.copy(
                        viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                        isLoading = false
                    )

                    else -> SettingsViewState()
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            runUseCase(logout, Unit)
            state = SettingsViewState()
        }
    }

    private fun isLoggedIn() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result =
                runUseCase(isLoggedInUseCase, Unit)
            state = when (result) {
                is Success -> state.copy(
                    isLoggedIn = result.data,
                    isLoading = false
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )

                else -> SettingsViewState()
            }
        }
    }

    fun getAccount() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result =
                runUseCase(getAccount, Unit)
            state = when (result) {
                is Success -> state.copy(
                    isLoading = false,
                    account = result.data,
                )

                is Fail -> state.copy(
                    viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                    isLoading = false
                )

                else -> SettingsViewState()
            }
        }

    }
}
