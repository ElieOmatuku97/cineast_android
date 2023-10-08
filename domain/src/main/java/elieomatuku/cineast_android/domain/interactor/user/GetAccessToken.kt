package elieomatuku.cineast_android.domain.interactor.user

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.NoInputUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.AccessToken
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetAccessToken @Inject constructor(private val authenticationRepository: AuthenticationRepository) :
    NoInputUseCase<CompleteResult<AccessToken>> {
    override suspend fun execute(params: Unit): CompleteResult<AccessToken> {
        return safeUseCaseCall {
            return@safeUseCaseCall authenticationRepository.getAccessToken()
        }
    }
}
