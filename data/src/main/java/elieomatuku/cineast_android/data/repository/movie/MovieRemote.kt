package elieomatuku.cineast_android.data.repository.movie


/**
 * Created by elieomatuku on 2021-07-04
 */

interface MovieRemote {
    suspend fun getPopularMovies(): elieomatuku.cineast_android.remote.model.RemoteMovies

    suspend fun getUpcomingMovies(): elieomatuku.cineast_android.remote.model.RemoteMovies

    suspend fun getNowPlayingMovies(): elieomatuku.cineast_android.remote.model.RemoteMovies

    suspend fun getTopRatedMovies(): elieomatuku.cineast_android.remote.model.RemoteMovies

    suspend fun getGenres(): elieomatuku.cineast_android.remote.model.RemoteGenres

    suspend fun getMovieVideos(movieId: Int): elieomatuku.cineast_android.remote.model.RemoteTrailers

    suspend fun getMovieFacts(movieId: Int): elieomatuku.cineast_android.remote.model.RemoteMovieFacts

    suspend fun getMovieCredits(movieId: Int): elieomatuku.cineast_android.remote.model.RemoteMovieCredits

    suspend fun getSimilarMovie(movieId: Int): elieomatuku.cineast_android.remote.model.RemoteMovies

    suspend fun getMovieImages(movieId: Int): elieomatuku.cineast_android.remote.model.RemoteImages

    suspend fun getMovie(movieId: Int): elieomatuku.cineast_android.remote.model.RemoteMovie

    suspend fun searchMovies(query: String): elieomatuku.cineast_android.remote.model.RemoteMovies

    suspend fun updateWatchList(
        sessionId: String,
        mediaRequest: elieomatuku.cineast_android.remote.request.WatchListMediaRequest
    ): elieomatuku.cineast_android.remote.model.RemotePostResult

    suspend fun getFavoriteList(sessionId: String): elieomatuku.cineast_android.remote.model.RemoteMovies

    suspend fun updateFavoriteList(
        sessionId: String,
        mediaRequest: elieomatuku.cineast_android.remote.request.FavouritesMediaRequest
    ): elieomatuku.cineast_android.remote.model.RemotePostResult

    suspend fun postMovieRate(
        movieId: Int,
        sessionId: String,
        requestBody: okhttp3.RequestBody
    ): elieomatuku.cineast_android.remote.model.RemotePostResult

    suspend fun getUserRatedMovies(sessionId: String): elieomatuku.cineast_android.remote.model.RemoteMovies
}