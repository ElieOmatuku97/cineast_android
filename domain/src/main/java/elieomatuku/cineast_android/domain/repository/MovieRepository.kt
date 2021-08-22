package elieomatuku.cineast_android.domain.repository

import elieomatuku.cineast_android.domain.model.*


/**
 * Created by elieomatuku on 2021-08-21
 */

interface MovieRepository {
    suspend fun discoverContent(): DiscoverContent

    suspend fun genres(): List<Genre>

    suspend fun getPopularMovies(): List<Movie>

    suspend fun getUpcomingMovies(): List<Movie>

    suspend fun getNowPlayingMovies(): List<Movie>

    suspend fun getTopRatedMovies(): List<Movie>

    suspend fun getMovieTrailers(movie: Movie): List<Trailer>

    suspend fun getMovieDetails(movie: Movie): MovieFacts?

    suspend fun getMovieCredits(movie: Movie): MovieCredits

    suspend fun getSimilarMovies(movie: Movie): List<Movie>

    suspend fun getMovieImages(movieId: Int): Images

    suspend fun getMovie(movieId: Int): Movie

    suspend fun searchMovies(argQuery: String): List<Movie>

    suspend fun getWatchList(): List<Movie>

    suspend fun getFavorites(): List<Movie>
}