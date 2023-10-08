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

class GetMovie @Inject constructor (private val movieRepository: MovieRepository) :
    UseCase<GetMovie.Input, CompleteResult<Movie>> {

    override suspend fun execute(params: Input): CompleteResult<Movie> {
        return safeUseCaseCall {
            return@safeUseCaseCall movieRepository.getMovie(
                params.movieId
            )
        }
    }

    data class Input(val movieId: Int)
}
