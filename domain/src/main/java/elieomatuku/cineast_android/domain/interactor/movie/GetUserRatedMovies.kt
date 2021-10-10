package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.NoInputUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository
import elieomatuku.cineast_android.domain.repository.MovieRepository


/**
 * Created by elieomatuku on 2021-10-10
 */

class GetUserRatedMovies(
    private val movieRepository: MovieRepository,
    private val authenticationRepository: AuthenticationRepository
) :  NoInputUseCase<CompleteResult<List<Movie>>>{

    override suspend fun execute(params: Unit): CompleteResult<List<Movie>> {
        return safeUseCaseCall {
            val requestToken = authenticationRepository.getRequestToken()
            val session = requestToken?.let { authenticationRepository.getSession(it) }
            return@safeUseCaseCall session?.sessionId?.let { movieRepository.getUserRatedMovies(it) }
                ?: listOf()
        }
    }
}