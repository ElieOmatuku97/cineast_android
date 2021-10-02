package elieomatuku.cineast_android.domain.interactor.people

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.domain.repository.PersonRepository

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetKnownForMovies(private val personRepository: PersonRepository) :
    UseCase<GetKnownForMovies.Input, CompleteResult<List<Movie>>> {

    override suspend fun execute(params: Input): CompleteResult<List<Movie>> {
        return safeUseCaseCall {
            return@safeUseCaseCall personRepository.getMovies(
                params.person
            )
        }
    }

    data class Input(val person: Person)
}
