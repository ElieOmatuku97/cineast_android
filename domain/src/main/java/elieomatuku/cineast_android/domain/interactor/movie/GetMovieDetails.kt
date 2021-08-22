package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.MovieFacts
import elieomatuku.cineast_android.domain.repository.MovieRepository


/**
 * Created by elieomatuku on 2021-08-22
 */

class GetMovieDetails(private val movieRepository: MovieRepository) :
    UseCase<GetMovieDetails.Input, CompleteResult<MovieFacts>> {

    override suspend fun execute(params: Input): CompleteResult<MovieFacts> {
        return safeUseCaseCall {
            return@safeUseCaseCall movieRepository.getMovieDetails(
                params.movie
            )
        }
    }

    data class Input(val movie: Movie)
}