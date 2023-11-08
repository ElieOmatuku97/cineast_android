package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetSimilarMovies @Inject constructor (private val movieRepository: MovieRepository) :
    UseCase<GetSimilarMovies.Input, CompleteResult<List<Movie>>> {

    override suspend fun execute(params: Input): CompleteResult<List<Movie>> {
        return safeUseCaseCall {
            return@safeUseCaseCall movieRepository.getSimilarMovies(
                params.movie
            )
        }
    }

    data class Input(val movie: Movie)
}
