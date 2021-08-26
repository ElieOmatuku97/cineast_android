package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.NoInputUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.DiscoverContent
import elieomatuku.cineast_android.domain.repository.MovieRepository
import elieomatuku.cineast_android.domain.repository.PersonRepository


/**
 * Created by elieomatuku on 2021-08-22
 */

class GetDiscoverContent(
    private val movieRepository: MovieRepository,
    private val personRepository: PersonRepository
) :
    NoInputUseCase<CompleteResult<DiscoverContent>> {
    override suspend fun execute(params: Unit): CompleteResult<DiscoverContent> {
        return safeUseCaseCall {
            val popularMovies = movieRepository.getPopularMovies()
            val nowPlayingMovies = movieRepository.getNowPlayingMovies()
            val upcomingMovies = movieRepository.getUpcomingMovies()
            val topRatedMovies = movieRepository.getTopRatedMovies()
            val people = personRepository.getPopularPeople()
            return@safeUseCaseCall DiscoverContent(
                popularMovies = popularMovies,
                nowPlayingMovies = nowPlayingMovies,
                upcomingMovies = upcomingMovies,
                topRatedMovies = topRatedMovies,
                people = people
            )
        }
    }
}