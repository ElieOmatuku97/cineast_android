package elieomatuku.cineast_android.domain.interactor.people

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.NoInputUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.domain.repository.PersonRepository
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetPersonalities @Inject constructor(private val personRepository: PersonRepository) :
    NoInputUseCase<CompleteResult<List<Person>>> {
    override suspend fun execute(params: Unit): CompleteResult<List<Person>> {
        return safeUseCaseCall {
            return@safeUseCaseCall personRepository.getPopularPeople()
        }
    }
}
