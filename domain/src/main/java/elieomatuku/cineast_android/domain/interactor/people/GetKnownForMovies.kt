package elieomatuku.cineast_android.domain.interactor.people

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.repository.PersonRepository
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetKnownForMovies @Inject constructor (private val personRepository: PersonRepository) :
    UseCase<GetKnownForMovies.Input, CompleteResult<List<Movie>>> {

    override suspend fun execute(params: Input): CompleteResult<List<Movie>> {
        return safeUseCaseCall {
            return@safeUseCaseCall personRepository.getMovies(
                params.personId
            )
        }
    }

    data class Input(val personId: Int)
}
