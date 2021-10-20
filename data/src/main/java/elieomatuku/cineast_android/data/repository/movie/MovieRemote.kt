package elieomatuku.cineast_android.data.repository.movie

import elieomatuku.cineast_android.data.model.*

/**
 * Created by elieomatuku on 2021-07-04
 */

interface MovieRemote {
    suspend fun getPopularMovies(): List<MovieEntity>

    suspend fun getUpcomingMovies(): List<MovieEntity>

    suspend fun getNowPlayingMovies(): List<MovieEntity>

    suspend fun getTopRatedMovies(): List<MovieEntity>

    suspend fun getGenres(): List<GenreEntity>

    suspend fun getTrailers(movieId: Int): List<TrailerEntity>

    suspend fun getMovieFacts(movieId: Int): MovieFactsEntity

    suspend fun getMovieCredits(movieId: Int): MovieCreditsEntity

    suspend fun getSimilarMovie(movieId: Int): List<MovieEntity>

    suspend fun getMovieImages(movieId: Int): ImageEntities

    suspend fun getMovie(movieId: Int): MovieEntity

    suspend fun searchMovies(query: String): List<MovieEntity>

    suspend fun getWatchList(sessionId: String): List<MovieEntity>

    suspend fun updateWatchList(
        sessionId: String,
        movie: MovieEntity,
        accountEntity: AccountEntity,
        watchList: Boolean
    ): PostResultEntity

    suspend fun getFavoriteList(sessionId: String): List<MovieEntity>

    suspend fun updateFavoriteList(
        sessionId: String,
        movie: MovieEntity,
        accountEntity: AccountEntity,
        favorite: Boolean
    ): PostResultEntity

    suspend fun postMovieRate(
        movieId: Int,
        sessionId: String,
        rate: Double
    ): PostResultEntity

    suspend fun getUserRatedMovies(sessionId: String): List<MovieEntity>
}
