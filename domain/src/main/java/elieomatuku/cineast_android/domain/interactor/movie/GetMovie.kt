package elieomatuku.cineast_android.domain.interactor.movie

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.Fail
import elieomatuku.cineast_android.domain.interactor.Success
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.interactor.user.IsLoggedIn
import elieomatuku.cineast_android.domain.model.FavoriteState
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.WatchListState
import elieomatuku.cineast_android.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class UnAuthedGetMovie @Inject constructor(
    private val movieRepository: MovieRepository,
    private val getMovieSummary: GetMovieSummary,
) : BaseGetMovie {

    override suspend fun execute(params: BaseGetMovie.Input): CompleteResult<Movie> {
        return safeUseCaseCall {
            val movie = movieRepository.getMovie(
                params.movieId
            )
            return@safeUseCaseCall when (val result =
                runUseCase(getMovieSummary, GetMovieSummary.Input(movie))) {
                is Success -> movie.copy(
                    movieSummary = result.data,
                    posters = result.data.posters,
                )

                else -> movie
            }
        }
    }
}

class AuthedGetMovie @Inject constructor (
    private val baseGetMovie: UnAuthedGetMovie,
    private val getWatchList: GetWatchList,
    private val getFavorites: GetFavorites,
) : BaseGetMovie {

    override suspend fun execute(params: BaseGetMovie.Input): CompleteResult<Movie> {
        return safeUseCaseCall {
            return@safeUseCaseCall when (val result =
                runUseCase(baseGetMovie, params)) {
                is Success -> {
                    applyWatchListsTag(
                        movie = applyFavoriteTag(
                            movie = result.data
                        )
                    )
                }

                is Fail -> throw result.throwable
            }
        }
    }

    private suspend fun applyFavoriteTag(movie: Movie): Movie {
        return when (val result = runUseCase(getFavorites, Unit)) {
            is Success -> {
                movie.copy(
                    favoritesState = FavoriteState(
                        isVisible = true,
                        isSelected = result.data.contains(movie)
                    )
                )
            }

            is Fail -> movie
        }
    }

    private suspend fun applyWatchListsTag(movie: Movie): Movie {
        return when (val result =
            runUseCase(getWatchList, Unit)) {
            is Success -> {
                movie.copy(
                    watchListState = WatchListState(
                        isVisible = true,
                        isSelected = result.data.contains(movie)
                    )
                )
            }

            is Fail -> movie
        }
    }

}

class GetMovieFactory @Inject constructor(
    private val isLoggedIn: IsLoggedIn,
    private val unAuthedUseCase: UnAuthedGetMovie,
    private val authedUseCase: AuthedGetMovie
) {

    suspend fun obtainGetMovieUseCase(): BaseGetMovie {
        val result = when (val result = runUseCase(isLoggedIn, Unit)) {
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
        return result
    }
}

interface BaseGetMovie : UseCase<BaseGetMovie.Input, CompleteResult<Movie>> {

    data class Input(val movieId: Int)
}
