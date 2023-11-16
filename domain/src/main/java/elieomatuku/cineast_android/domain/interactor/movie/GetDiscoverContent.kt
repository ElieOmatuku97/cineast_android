package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.NoInputUseCase
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.interactor.user.IsLoggedIn
import elieomatuku.cineast_android.domain.model.Discover
import elieomatuku.cineast_android.domain.model.DiscoverContents
import elieomatuku.cineast_android.domain.repository.MovieRepository
import elieomatuku.cineast_android.domain.repository.PersonRepository
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetDiscoverContent @Inject constructor(
    private val movieRepository: MovieRepository,
    private val personRepository: PersonRepository
) :
    NoInputUseCase<CompleteResult<Discover>> {
    override suspend fun execute(params: Unit): CompleteResult<Discover> {
        return safeUseCaseCall {
            val popularMovies = movieRepository.getPopularMovies()
            val nowPlayingMovies = movieRepository.getNowPlayingMovies()
            val upcomingMovies = movieRepository.getUpcomingMovies()
            val topRatedMovies = movieRepository.getTopRatedMovies()
            val people = personRepository.getPopularPeople()
            return@safeUseCaseCall Discover(
                content = DiscoverContents(
                    popularMovies = popularMovies,
                    nowPlayingMovies = nowPlayingMovies,
                    upcomingMovies = upcomingMovies,
                    topRatedMovies = topRatedMovies,
                    people = people
                )
            )
        }
    }
}

class AuthedGetDiscoverContent @Inject constructor(
    private val unAuthedUseCase: GetDiscoverContent,
) : NoInputUseCase<CompleteResult<Discover>> {
    override suspend fun execute(params: Unit): CompleteResult<Discover> {
        return safeUseCaseCall {
            return@safeUseCaseCall when (val result =
                runUseCase(unAuthedUseCase, params)) {
                is Success -> {
                    result.data.copy(
                        isLoggedIn = true
                    )
                }

                is Fail -> throw result.throwable
            }
        }
    }
}

class GetDiscoverContentFactory @Inject constructor(
    private val isLoggedIn: IsLoggedIn,
    private val unAuthedUseCase: GetDiscoverContent,
    private val authedUseCase: AuthedGetDiscoverContent
) {

    suspend fun obtainUseCase(): NoInputUseCase<CompleteResult<Discover>> {
        val getMovie = when (val result = runUseCase(isLoggedIn, Unit)) {
            is Success -> {
                if (result.data) {
                    authedUseCase
                } else {
                    unAuthedUseCase
                }
            }

            is Fail -> {
                unAuthedUseCase
            }
        }
        return getMovie
    }
}