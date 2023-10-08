package elieomatuku.cineast_android.data

import elieomatuku.cineast_android.data.model.*
import elieomatuku.cineast_android.data.source.movie.MovieDataStoreFactory
import elieomatuku.cineast_android.domain.model.*
import elieomatuku.cineast_android.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class MovieRepositoryImpl @Inject constructor(private val factory: MovieDataStoreFactory) : MovieRepository {
    override suspend fun genres(): List<Genre> {
        val genres = factory.retrieveDataStore().genres()
        return genres.map {
            it.let(GenreEntity::toGenre)
        }
    }

    override suspend fun getPopularMovies(): List<Movie> {
        return factory.retrieveDataStore().getPopularMovies().map {
            it.let(MovieEntity::toMovie)
        }
    }

    override suspend fun getUpcomingMovies(): List<Movie> {
        return factory.retrieveDataStore().getUpcomingMovies().map {
            it.let(MovieEntity::toMovie)
        }
    }

    override suspend fun getNowPlayingMovies(): List<Movie> {
        return factory.retrieveDataStore().getNowPlayingMovies().map {
            it.let(MovieEntity::toMovie)
        }
    }

    override suspend fun getTopRatedMovies(): List<Movie> {
        return factory.retrieveDataStore().getTopRatedMovies().map {
            it.let(MovieEntity::toMovie)
        }
    }

    override suspend fun getMovieTrailers(movie: Movie): List<Trailer> {
        return factory.retrieveRemoteDataStore().getMovieTrailers(movie.let(MovieEntity::fromMovie))
            .map {
                it.let(TrailerEntity::toTrailer)
            }
    }

    override suspend fun getMovieDetails(movie: Movie): MovieFacts {
        return factory.retrieveRemoteDataStore().getMovieDetails(movie.let(MovieEntity::fromMovie))
            .let(MovieFactsEntity::toMovieFacts)
    }

    override suspend fun getMovieCredits(movie: Movie): MovieCredits {
        return factory.retrieveRemoteDataStore().getMovieCredits(movie.let(MovieEntity::fromMovie))
            .let(
                MovieCreditsEntity::toMovieCredits
            )
    }

    override suspend fun getSimilarMovies(movie: Movie): List<Movie> {
        return factory.retrieveRemoteDataStore()
            .getSimilarMovies(movie.let(MovieEntity::fromMovie)).map {
                it.let(MovieEntity::toMovie)
            }
    }

    override suspend fun getMovieImages(movieId: Int): Images {
        return factory.retrieveRemoteDataStore().getMovieImages(movieId)
            .let(ImageEntities::toImages)
    }

    override suspend fun getMovie(movieId: Int): Movie {
        return factory.retrieveRemoteDataStore().getMovie(movieId).let(MovieEntity::toMovie)
    }

    override suspend fun searchMovies(argQuery: String): List<Movie> {
        return factory.retrieveRemoteDataStore().searchMovies(argQuery).map {
            it.let(MovieEntity::toMovie)
        }
    }

    override suspend fun getWatchList(sessionId: String): List<Movie> {
        return factory.retrieveRemoteDataStore().getWatchList(sessionId).map {
            it.let(MovieEntity::toMovie)
        }
    }

    override suspend fun getFavorites(sessionId: String): List<Movie> {
        return factory.retrieveRemoteDataStore().getFavorites(sessionId).map {
            it.let(MovieEntity::toMovie)
        }
    }

    override suspend fun updateWatchList(
        sessionId: String,
        movie: Movie,
        account: Account,
        watchList: Boolean
    ) {
        factory.retrieveRemoteDataStore()
            .updateWatchList(
                sessionId,
                movie.let(MovieEntity::fromMovie),
                account.let(AccountEntity::fromAccount),
                watchList
            )
    }

    override suspend fun updateFavoriteList(
        sessionId: String,
        movie: Movie,
        account: Account,
        favorite: Boolean
    ) {
        factory.retrieveRemoteDataStore()
            .updateFavoriteList(
                sessionId,
                movie.let(MovieEntity::fromMovie),
                account.let(AccountEntity::fromAccount),
                favorite
            )
    }

    override suspend fun postMovieRate(movieId: Int, sessionId: String, rate: Double) {
        factory.retrieveRemoteDataStore()
            .postMovieRate(movieId, sessionId, rate)
    }

    override suspend fun getUserRatedMovies(sessionId: String): List<Movie> {
        return factory.retrieveRemoteDataStore().getUserRatedMovies(sessionId).map {
            it.let(MovieEntity::toMovie)
        }
    }
}
