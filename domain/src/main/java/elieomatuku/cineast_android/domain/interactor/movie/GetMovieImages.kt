package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Images
import elieomatuku.cineast_android.domain.repository.MovieRepository


/**
 * Created by elieomatuku on 2021-08-22
 */

class GetMovieImages(private val movieRepository: MovieRepository) :
    UseCase<GetMovieImages.Input, CompleteResult<Images>> {

    override suspend fun execute(params: Input): CompleteResult<Images> {
        return safeUseCaseCall {
            return@safeUseCaseCall movieRepository.getMovieImages(
                params.movieId
            )
        }
    }

    data class Input(val movieId: Int)
}