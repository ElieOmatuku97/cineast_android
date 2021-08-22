package elieomatuku.cineast_android.domain.interactor.user

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository


/**
 * Created by elieomatuku on 2021-08-22
 */

class GetSession(private val authenticationRepository: AuthenticationRepository) :
    UseCase<GetSession.Input, CompleteResult<Pair<String, Account>>> {

    override suspend fun execute(params: Input): CompleteResult<Pair<String, Account>> {
        return safeUseCaseCall {
            return@safeUseCaseCall authenticationRepository.getSession(
                params.requestToken
            )
        }
    }

    data class Input(val requestToken: String)

}