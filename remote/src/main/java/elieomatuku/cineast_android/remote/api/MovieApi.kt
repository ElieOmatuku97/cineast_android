package elieomatuku.cineast_android.remote.api

import elieomatuku.cineast_android.remote.api.model.*
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Call
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
    fun getTopRatedMovies(): Deferred<RemoteMovies>

    @GET(UPCOMING_MOVIE)
    fun getUpcomingMovies(): Deferred<RemoteMovies>

    @GET(POPULAR_MOVIE)
    fun getPopularMovie(): Deferred<RemoteMovies>

    @GET(NOW_PLAYING_MOVIE)
    fun getNowPlayingMovie(): Deferred<RemoteMovies>

    @GET(MOVIE_VIDEOS)
    fun getMovieVideos(@Path(MOVIE_ID) movie_id: Int): Deferred<RemoteTrailers>

    @GET(MOVIE_DETAILS)
    fun getMovieDetails(@Path(MOVIE_ID) movie_id: Int): Deferred<RemoteMovieFacts>

    @GET(MOVIE)
    fun getMovie(@Path(MOVIE_ID) movie_id: Int): Call<RemoteMovie>

    @GET(GENRE)
    fun getGenre(): Deferred<RemoteGenres>

    @GET(CREDITS)
    fun getCredits(@Path(MOVIE_ID) movie_id: Int): Deferred<RemoteMovieCredits>

    @GET(SIMILAR_MOVIE)
    fun getSimilarMovie(@Path(MOVIE_ID) movie_id: Int): Deferred<RemoteMovies>

    @GET(MOVIE_IMAGE)
    fun getMovieImages(@Path(MOVIE_ID) movie_id: Int): Deferred<RemoteImages>

    @GET(SEARCH_MOVIE)
    fun getMoviesWithSearch(@Query(QUERY) query: String): Call<RemoteMovies>

    @GET(WATCHLIST_MOVIE)
    fun getWatchList(@Query(SESSION_ID) sessionId: String): Deferred<RemoteMovies>

    @POST(UPDATE_WATCHLIST_MOVIE)
    fun updateWatchList(
        @Query(SESSION_ID) sessionId: String,
        @Body media: RequestBody
    ): Call<RemotePost>

    @GET(FAVORITES_MOVIE)
    fun getFavoritesList(@Query(SESSION_ID) sessionId: String): Deferred<RemoteMovies>

    @POST(UPDATE_FAVORITES_MOVIE)
    fun updateFavoritesList(
        @Query(SESSION_ID) sessionId: String,
        @Body media: RequestBody
    ): Call<RemotePost>

    @GET(RATED_MOVIE)
    fun getUserRatedMovies(@Query(SESSION_ID) sessionId: String): Call<RemoteMovies>

    @POST(POST_MOVIE_RATE)
    fun postMovieRate(
        @Path(MOVIE_ID) movieId: Int,
        @Query(SESSION_ID) sessionId: String,
        @Body value: RequestBody
    ): Call<RemotePost>
}
