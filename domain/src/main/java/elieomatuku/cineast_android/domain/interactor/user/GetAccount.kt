package elieomatuku.cineast_android.domain.interactor.user

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.NoInputUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetAccount(private val authenticationRepository: AuthenticationRepository) :
    NoInputUseCase<CompleteResult<Account?>> {
    override suspend fun execute(params: Unit): CompleteResult<Account?> {
        return safeUseCaseCall {
            return@safeUseCaseCall authenticationRepository.getAccount()
        }
    }
}
