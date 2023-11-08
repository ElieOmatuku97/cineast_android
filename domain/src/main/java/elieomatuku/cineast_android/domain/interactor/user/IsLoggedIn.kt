package elieomatuku.cineast_android.domain.interactor.user

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.NoInputUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class IsLoggedIn @Inject constructor(private val authenticationRepository: AuthenticationRepository) :
    NoInputUseCase<CompleteResult<Boolean>> {
    override suspend fun execute(params: Unit): CompleteResult<Boolean> {
        return safeUseCaseCall {
            return@safeUseCaseCall authenticationRepository.isLoggedIn()
        }
    }
}
