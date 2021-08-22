package elieomatuku.cineast_android.data.source.movie

import elieomatuku.cineast_android.data.model.*
import elieomatuku.cineast_android.data.repository.movie.MovieDataStore
import elieomatuku.cineast_android.data.repository.movie.MovieRemote


/**
 * Created by elieomatuku on 2021-08-22
 */

class MovieRemoteDataStore(private val movieRemote: MovieRemote): MovieDataStore {
    override suspend fun genres(): List<GenreEntity> {
        return movieRemote.getGenres()
    }

    override suspend fun getPopularMovies(): List<MovieEntity> {
        return movieRemote.getPopularMovies()
    }

    override suspend fun getUpcomingMovies(): List<MovieEntity> {
        return movieRemote.getUpcomingMovies()
    }

    override suspend fun getNowPlayingMovies(): List<MovieEntity> {
        return movieRemote.getNowPlayingMovies()
    }

    override suspend fun getTopRatedMovies(): List<MovieEntity> {
        return movieRemote.getTopRatedMovies()
    }

    override suspend fun getMovieTrailers(movie: MovieEntity): List<TrailerEntity> {
        return movieRemote.getTrailers(movie.id)
    }

    override suspend fun getMovieDetails(movie: MovieEntity): MovieFactsEntity {
        return movieRemote.getMovieFacts(movie.id)
    }

    override suspend fun getMovieCredits(movie: MovieEntity): MovieCreditsEntity {
        return movieRemote.getMovieCredits(movie.id)
    }

    override suspend fun getSimilarMovies(movie: MovieEntity): List<MovieEntity> {
        return movieRemote.getSimilarMovie(movie.id)
    }

    override suspend fun getMovieImages(movieId: Int): ImageEntities {
       return movieRemote.getMovieImages(movieId)
    }

    override suspend fun getMovie(movieId: Int): MovieEntity {
        return movieRemote.getMovie(movieId)
    }

    override suspend fun searchMovies(argQuery: String): List<MovieEntity> {
        return movieRemote.searchMovies(argQuery)
    }

    override suspend fun getWatchList(): List<MovieEntity> {
        return movieRemote.
    }

    override suspend fun getFavorites(): List<MovieEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllMovies() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMovie(movie: MovieEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun insertGenres(genres: List<GenreEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertTopRatedMovie(movies: List<MovieEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertNowPlayingMovie(movies: List<MovieEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertUpcomingMovie(movies: List<MovieEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertPopularMovie(movies: List<MovieEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertMovie(movie: MovieEntity, type: MovieType) {
        TODO("Not yet implemented")
    }

    override suspend fun updateMovie(movie: MovieEntity) {
        TODO("Not yet implemented")
    }
}