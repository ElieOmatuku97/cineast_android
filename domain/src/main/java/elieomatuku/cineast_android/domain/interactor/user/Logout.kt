package elieomatuku.cineast_android.domain.interactor.user

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.NoInputUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository


/**
 * Created by elieomatuku on 2021-08-22
 */

class Logout(private val authenticationRepository: AuthenticationRepository) :
    NoInputUseCase<CompleteResult<Unit>> {
    override suspend fun execute(params: Unit): CompleteResult<Unit> {
        return safeUseCaseCall {
            return@safeUseCaseCall authenticationRepository.logout()
        }
    }
}