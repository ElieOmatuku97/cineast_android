package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.repository.MovieRepository

/**
 * Created by elieomatuku on 2021-08-22
 */

class SearchMovies(private val movieRepository: MovieRepository) :
    UseCase<SearchMovies.Input, CompleteResult<List<Movie>>> {

    override suspend fun execute(params: Input): CompleteResult<List<Movie>> {
        return safeUseCaseCall {
            return@safeUseCaseCall movieRepository.searchMovies(
                params.argQuery
            )
        }
    }

    data class Input(val argQuery: String)
}
