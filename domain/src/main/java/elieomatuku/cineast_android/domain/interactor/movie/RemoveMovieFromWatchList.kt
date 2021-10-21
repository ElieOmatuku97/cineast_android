package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.NoOutputUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository
import elieomatuku.cineast_android.domain.repository.MovieRepository


/**
 * Created by elieomatuku on 2021-09-12
 */

class RemoveMovieFromWatchList(
    private val movieRepository: MovieRepository,
    private val authenticationRepository: AuthenticationRepository
) :
    NoOutputUseCase<RemoveMovieFromWatchList.Input> {

    override suspend fun execute(params: Input) {
        safeUseCaseCall {
            val requestToken = authenticationRepository.getAccessToken().requestToken
            val account = authenticationRepository.getAccount()
            val session = requestToken?.let { authenticationRepository.getSession(it) }

            session?.sessionId?.apply {
                account?.let { account ->
                    movieRepository.updateWatchList(this, params.movie, account, false)
                }
            }
        }
    }

    data class Input(val movie: Movie)
}