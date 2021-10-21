package elieomatuku.cineast_android.domain.interactor.user

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Session
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetSession(private val authenticationRepository: AuthenticationRepository) :
    UseCase<GetSession.Input, CompleteResult<Session>> {

    override suspend fun execute(params: Input): CompleteResult<Session> {
        return safeUseCaseCall {
            return@safeUseCaseCall authenticationRepository.getSession(
                params.requestToken
            )
        }
    }

    data class Input(val requestToken: String)
}
