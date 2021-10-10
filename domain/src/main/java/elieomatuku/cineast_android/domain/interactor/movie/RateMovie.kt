package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.NoOutputUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository
import elieomatuku.cineast_android.domain.repository.MovieRepository


/**
 * Created by elieomatuku on 2021-10-10
 */

class RateMovie(
    private val movieRepository: MovieRepository,
    private val authenticationRepository: AuthenticationRepository
) :
    NoOutputUseCase<RateMovie.Input> {

    override suspend fun execute(params: RateMovie.Input) {
        safeUseCaseCall {
            val requestToken = authenticationRepository.getRequestToken()
            val session = requestToken?.let { authenticationRepository.getSession(it) }

            session?.sessionId?.apply {
                movieRepository.postMovieRate(params.movie.id, this, params.rate)
            }
        }
    }

    data class Input(val movie: Movie, val rate: Double)
}