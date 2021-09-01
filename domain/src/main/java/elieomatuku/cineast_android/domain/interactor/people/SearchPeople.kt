package elieomatuku.cineast_android.domain.interactor.people

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.domain.repository.PersonRepository

/**
 * Created by elieomatuku on 2021-08-22
 */

class SearchPeople(private val personRepository: PersonRepository) :
    UseCase<SearchPeople.Input, CompleteResult<List<Person>>> {

    override suspend fun execute(params: Input): CompleteResult<List<Person>> {
        return safeUseCaseCall {
            return@safeUseCaseCall personRepository.searchPeople(
                params.argQuery
            )
        }
    }

    data class Input(val argQuery: String)
}
