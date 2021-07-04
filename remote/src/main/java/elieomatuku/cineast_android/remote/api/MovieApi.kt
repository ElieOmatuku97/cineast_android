package elieomatuku.cineast_android.remote.api

import elieomatuku.cineast_android.remote.model.RemoteGenres
import elieomatuku.cineast_android.remote.model.RemoteImages
import elieomatuku.cineast_android.remote.model.RemoteMovie
import elieomatuku.cineast_android.remote.model.RemoteMovieCredits
import elieomatuku.cineast_android.remote.model.RemoteMovieFacts
import elieomatuku.cineast_android.remote.model.RemoteMovies
import elieomatuku.cineast_android.remote.model.RemotePostResult
import elieomatuku.cineast_android.remote.model.RemoteTrailers
import elieomatuku.cineast_android.remote.request.FavouritesMediaRequest
import elieomatuku.cineast_android.remote.request.WatchListMediaRequest
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    companion object {
        private const val MOVIE_ID = "movie_id"
        private const val SESSION_ID = "session_id"
        private const val ACCOUNT_ID = "account_id"
        const val QUERY = "query"

        const val TOP_RATED_MOVIE = "movie/top_rated"
        const val UPCOMING_MOVIE = "movie/upcoming"
        const val POPULAR_MOVIE = "movie/popular"
        const val NOW_PLAYING_MOVIE = "movie/now_playing"
        const val MOVIE_VIDEOS = "movie/{$MOVIE_ID}/videos"
        const val MOVIE_DETAILS = "movie/{$MOVIE_ID}"
        const val MOVIE = "movie/{$MOVIE_ID}"
        const val GENRE = "genre/movie/list"
        const val CREDITS = "movie/{$MOVIE_ID}/credits"
        const val SIMILAR_MOVIE = "movie/{$MOVIE_ID}/similar"
        const val MOVIE_IMAGE = "movie/{$MOVIE_ID}/images"
        const val SEARCH_MOVIE = "search/movie"
        const val WATCHLIST_MOVIE = "account/{$ACCOUNT_ID}/watchlist/movies"
        const val UPDATE_WATCHLIST_MOVIE = "account/{$ACCOUNT_ID}/watchlist"
        const val FAVORITES_MOVIE = "account/{account_id}/favorite/movies"
        const val UPDATE_FAVORITES_MOVIE = "account/{$ACCOUNT_ID}/favorite"
        const val RATED_MOVIE = "account/{$ACCOUNT_ID}/rated/movies"
        const val POST_MOVIE_RATE = "movie/{$MOVIE_ID}/rating"
    }

    @GET(TOP_RATED_MOVIE)
    suspend fun getTopRatedMovies(): Response<RemoteMovies>

    @GET(UPCOMING_MOVIE)
    suspend fun getUpcomingMovies(): Response<RemoteMovies>

    @GET(POPULAR_MOVIE)
    suspend fun getPopularMovies(): Response<RemoteMovies>

    @GET(NOW_PLAYING_MOVIE)
    suspend fun getNowPlayingMovies(): Response<RemoteMovies>

    @GET(MOVIE_VIDEOS)
    suspend fun getMovieVideos(@Path(MOVIE_ID) movie_id: Int): Response<RemoteTrailers>

    @GET(MOVIE_DETAILS)
    suspend fun getMovieDetails(@Path(MOVIE_ID) movie_id: Int): Response<RemoteMovieFacts>

    @GET(MOVIE)
    suspend fun getMovie(@Path(MOVIE_ID) movie_id: Int): Response<RemoteMovie>

    @GET(GENRE)
    suspend fun getGenre(): Response<RemoteGenres>

    @GET(CREDITS)
    suspend fun getCredits(@Path(MOVIE_ID) movie_id: Int): Response<RemoteMovieCredits>

    @GET(SIMILAR_MOVIE)
    suspend fun getSimilarMovies(@Path(MOVIE_ID) movie_id: Int): Response<RemoteMovies>

    @GET(MOVIE_IMAGE)
    suspend fun getMovieImages(@Path(MOVIE_ID) movie_id: Int): Response<RemoteImages>

    @GET(SEARCH_MOVIE)
    suspend fun getMoviesWithSearch(@Query(QUERY) query: String): Response<RemoteMovies>

    @GET(WATCHLIST_MOVIE)
    suspend fun getWatchList(@Query(SESSION_ID) sessionId: String): Response<RemoteMovies>

    @POST(UPDATE_WATCHLIST_MOVIE)
    suspend fun updateWatchList(
        @Query(SESSION_ID) sessionId: String,
        @Body media: WatchListMediaRequest
    ): Response<RemotePostResult>

    @GET(FAVORITES_MOVIE)
    suspend fun getFavoritesList(@Query(SESSION_ID) sessionId: String): Response<RemoteMovies>

    @POST(UPDATE_FAVORITES_MOVIE)
    suspend fun updateFavorites(
        @Query(SESSION_ID) sessionId: String,
        @Body media: FavouritesMediaRequest
    ): Response<RemotePostResult>

    @GET(RATED_MOVIE)
    suspend fun getUserRatedMovies(@Query(SESSION_ID) sessionId: String): Response<RemoteMovies>

    @POST(POST_MOVIE_RATE)
    suspend fun postMovieRate(
        @Path(MOVIE_ID) movieId: Int,
        @Query(SESSION_ID) sessionId: String,
        @Body value: RequestBody
    ): Response<RemotePostResult>
}
