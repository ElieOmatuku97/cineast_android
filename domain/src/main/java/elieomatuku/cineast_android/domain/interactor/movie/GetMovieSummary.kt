package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.domain.repository.MovieRepository


/**
 * Created by elieomatuku on 2021-09-12
 */

class GetMovieSummary(private val movieRepository: MovieRepository) :
    UseCase<GetMovieSummary.Input, CompleteResult<MovieSummary>> {
    override suspend fun execute(params: Input): CompleteResult<MovieSummary> {
        return safeUseCaseCall {

            val trailers = movieRepository.getMovieTrailers(params.movie)
            val movieFacts = movieRepository.getMovieDetails(params.movie)
            val movieCredits = movieRepository.getMovieCredits(params.movie)
            val cast = movieCredits.cast
            val crew = movieCredits.crew
            val similarMovies = movieRepository.getSimilarMovies(params.movie)
            val genres = movieRepository.genres()
            val posters = movieRepository.getMovieImages(params.movie.id).posters

            return@safeUseCaseCall MovieSummary(
                movie = params.movie,
                trailers = trailers,
                facts = movieFacts,
                genres = genres,
                cast = cast,
                crew = crew,
                similarMovies = similarMovies,
                posters = posters
            )
        }
    }

    data class Input(val movie: Movie)
}