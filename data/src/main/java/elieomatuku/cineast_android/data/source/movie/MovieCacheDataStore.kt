package elieomatuku.cineast_android.data.source.movie

import elieomatuku.cineast_android.data.model.*
import elieomatuku.cineast_android.data.repository.movie.MovieCache
import elieomatuku.cineast_android.data.repository.movie.MovieDataStore


/**
 * Created by elieomatuku on 2021-08-22
 */

class MovieCacheDataStore(private val movieCache: MovieCache) : MovieDataStore {
    override suspend fun genres(): List<GenreEntity> {
        return movieCache.getGenres()
    }

    override suspend fun getPopularMovies(): List<MovieEntity> {
        return movieCache.getPopularMovies()
    }

    override suspend fun getUpcomingMovies(): List<MovieEntity> {
        return movieCache.getUpcomingMovies()
    }

    override suspend fun getNowPlayingMovies(): List<MovieEntity> {
        return movieCache.getNowPlayingMovies()
    }

    override suspend fun getTopRatedMovies(): List<MovieEntity> {
        return movieCache.getTopRatedMovies()
    }

    override suspend fun getMovieTrailers(movie: MovieEntity): List<TrailerEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieDetails(movie: MovieEntity): MovieFactsEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieCredits(movie: MovieEntity): MovieCreditsEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getSimilarMovies(movie: MovieEntity): List<MovieEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieImages(movieId: Int): ImageEntities {
        TODO("Not yet implemented")
    }

    override suspend fun getMovie(movieId: Int): MovieEntity {
        TODO("Not yet implemented")
    }

    override suspend fun searchMovies(argQuery: String): List<MovieEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getWatchList(sessionId: String): List<MovieEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavorites(sessionId: String): List<MovieEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllMovies() {
        movieCache.deleteAllMovies()
    }

    override suspend fun deleteMovie(movie: MovieEntity) {
        movieCache.deleteMovie(movie)
    }

    override suspend fun insertGenres(genres: List<GenreEntity>) {
        movieCache.insertGenres(genres)
    }

    override suspend fun insertTopRatedMovie(movies: List<MovieEntity>) {
        movieCache.insertTopRatedMovie(movies)
    }

    override suspend fun insertNowPlayingMovie(movies: List<MovieEntity>) {
        movieCache.insertNowPlayingMovie(movies)
    }

    override suspend fun insertUpcomingMovie(movies: List<MovieEntity>) {
        movieCache.insertUpcomingMovie(movies)
    }

    override suspend fun insertPopularMovie(movies: List<MovieEntity>) {
        movieCache.insertPopularMovie(movies)
    }

    override suspend fun insertMovie(movie: MovieEntity, type: MovieType) {
        movieCache.insertMovie(movie, type)
    }

    override suspend fun updateMovie(movie: MovieEntity) {
        movieCache.updateMovie(movie)
    }

    override suspend fun updateWatchList(
        sessionId: String,
        movie: MovieEntity,
        watchList: Boolean
    ): PostResultEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteList(sessionId: String): List<MovieEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun updateFavoriteList(
        sessionId: String,
        movie: MovieEntity,
        favorite: Boolean
    ): PostResultEntity {
        TODO("Not yet implemented")
    }

    override suspend fun postMovieRate(
        movieId: Int,
        sessionId: String,
        rate: Double
    ): PostResultEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getUserRatedMovies(sessionId: String): List<MovieEntity> {
        TODO("Not yet implemented")
    }
}