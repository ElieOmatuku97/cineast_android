package elieomatuku.cineast_android.domain.interactor.people

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.domain.model.PersonDetails
import elieomatuku.cineast_android.domain.repository.PersonRepository

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetDetails(private val personRepository: PersonRepository) :
    UseCase<GetDetails.Input, CompleteResult<PersonDetails>> {

    override suspend fun execute(params: Input): CompleteResult<PersonDetails> {
        return safeUseCaseCall {
            return@safeUseCaseCall personRepository.getDetails(
                params.person
            )
        }
    }

    data class Input(val person: Person)
}
