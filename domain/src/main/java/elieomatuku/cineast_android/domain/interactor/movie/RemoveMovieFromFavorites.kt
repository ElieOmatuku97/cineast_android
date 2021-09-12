package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.NoOutputUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository
import elieomatuku.cineast_android.domain.repository.MovieRepository


/**
 * Created by elieomatuku on 2021-09-12
 */

class RemoveMovieFromFavorites(
    private val movieRepository: MovieRepository,
    private val authenticationRepository: AuthenticationRepository
) :
    NoOutputUseCase<RemoveMovieFromFavorites.Input> {

    override suspend fun execute(params: Input) {
        safeUseCaseCall {
            val requestToken = authenticationRepository.getRequestToken()
            val session = requestToken?.let { authenticationRepository.getSession(it) }

            session?.sessionId?.apply {
                movieRepository.updateFavoriteList(this, params.movie, false)
            }
        }
    }

    data class Input(val movie: Movie)
}