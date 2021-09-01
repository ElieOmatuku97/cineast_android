package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.repository.MovieRepository

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetFavorites(private val movieRepository: MovieRepository) :
    UseCase<GetFavorites.Input, CompleteResult<List<Movie>>> {
    override suspend fun execute(params: GetFavorites.Input): CompleteResult<List<Movie>> {
        return safeUseCaseCall {
            return@safeUseCaseCall movieRepository.getFavorites(params.sessionId)
        }
    }

    data class Input(val sessionId: String)
}
