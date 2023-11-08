package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.NoInputUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetGenres @Inject constructor (private val movieRepository: MovieRepository) :
    NoInputUseCase<CompleteResult<List<Genre>>> {
    override suspend fun execute(params: Unit): CompleteResult<List<Genre>> {
        return safeUseCaseCall {
            return@safeUseCaseCall movieRepository.genres()
        }
    }
}
