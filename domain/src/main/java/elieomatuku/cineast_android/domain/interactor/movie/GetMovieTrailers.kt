package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Trailer
import elieomatuku.cineast_android.domain.repository.MovieRepository

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetMovieTrailers(private val movieRepository: MovieRepository) :
    UseCase<GetMovieTrailers.Input, CompleteResult<List<Trailer>>> {

    override suspend fun execute(params: Input): CompleteResult<List<Trailer>> {
        return safeUseCaseCall {

            return@safeUseCaseCall movieRepository.getMovieTrailers(
                params.movie
            )
        }
    }

    data class Input(val movie: Movie)
}
