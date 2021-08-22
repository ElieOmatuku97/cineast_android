package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.NoInputUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.DiscoverContent
import elieomatuku.cineast_android.domain.repository.MovieRepository


/**
 * Created by elieomatuku on 2021-08-22
 */

class GetDiscoverContent(private val movieRepository: MovieRepository) :
    NoInputUseCase<CompleteResult<DiscoverContent>> {
    override suspend fun execute(params: Unit): CompleteResult<DiscoverContent> {
        return safeUseCaseCall {
            return@safeUseCaseCall movieRepository.discoverContent()
        }
    }
}