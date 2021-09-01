package elieomatuku.cineast_android.domain.interactor.user

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository

/**
 * Created by elieomatuku on 2021-08-22
 */

class SetAccount(private val authenticationRepository: AuthenticationRepository) :
    UseCase<SetAccount.Input, CompleteResult<Pair<String, Account>>> {

    override suspend fun execute(params: Input): CompleteResult<Pair<String, Account>> {
        return safeUseCaseCall {
            return@safeUseCaseCall authenticationRepository.setAccount(
                params.sessionId
            )
        }
    }

    data class Input(val sessionId: String?)
}
