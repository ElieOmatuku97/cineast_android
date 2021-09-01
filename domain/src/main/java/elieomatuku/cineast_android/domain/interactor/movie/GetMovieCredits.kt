package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.MovieCredits
import elieomatuku.cineast_android.domain.repository.MovieRepository

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetMovieCredits(private val movieRepository: MovieRepository) :
    UseCase<GetMovieCredits.Input, CompleteResult<MovieCredits>> {

    override suspend fun execute(params: Input): CompleteResult<MovieCredits> {
        return safeUseCaseCall {
            return@safeUseCaseCall movieRepository.getMovieCredits(
                params.movie
            )
        }
    }

    data class Input(val movie: Movie)
}
