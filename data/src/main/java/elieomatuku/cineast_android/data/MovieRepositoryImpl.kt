package elieomatuku.cineast_android.data

import elieomatuku.cineast_android.data.model.GenreEntity
import elieomatuku.cineast_android.data.source.movie.MovieDataStoreFactory
import elieomatuku.cineast_android.domain.model.*
import elieomatuku.cineast_android.domain.repository.MovieRepository


/**
 * Created by elieomatuku on 2021-08-22
 */

class MovieRepositoryImpl(private val factory: MovieDataStoreFactory): MovieRepository {
    override suspend fun genres(): List<Genre> {
        val genres = factory.retrieveDataStore().genres()
        return genres.map {
            it.let(GenreEntity::toGenre)
        }
    }

    override suspend fun getPopularMovies(): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getUpcomingMovies(): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getNowPlayingMovies(): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getTopRatedMovies(): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieTrailers(movie: Movie): List<Trailer> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieDetails(movie: Movie): MovieFacts {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieCredits(movie: Movie): MovieCredits {
        TODO("Not yet implemented")
    }

    override suspend fun getSimilarMovies(movie: Movie): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieImages(movieId: Int): Images {
        TODO("Not yet implemented")
    }

    override suspend fun getMovie(movieId: Int): Movie {
        TODO("Not yet implemented")
    }

    override suspend fun searchMovies(argQuery: String): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getWatchList(): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavorites(): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun updateWatchList(sessionId: String, movie: Movie, watchList: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteList(sessionId: String): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun updateFavoriteList(sessionId: String, movie: Movie, favorite: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun postMovieRate(movieId: Int, sessionId: String, rate: Double) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserRatedMovies(sessionId: String): List<Movie> {
        TODO("Not yet implemented")
    }
}