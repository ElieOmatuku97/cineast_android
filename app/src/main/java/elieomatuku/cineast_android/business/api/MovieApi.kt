package elieomatuku.cineast_android.business.api

import elieomatuku.cineast_android.business.api.response.PostResponse
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.core.model.MovieFacts
import elieomatuku.cineast_android.business.api.response.*
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface MovieApi {
    companion object {

        const val API_KEY = "api_key"
        const val MOVIE_ID = "movie_id"
        const val SESSION_ID = "session_id"
        const val ACCOUNT_ID = "account_id"
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
    fun getTopRatedMovies(@Query(API_KEY) apyKey: String): Deferred<MovieResponse>

    @GET(UPCOMING_MOVIE)
    fun getUpcomingMovies(@Query(API_KEY) apiKey: String): Deferred<MovieResponse>

    @GET(POPULAR_MOVIE)
    fun getPopularMovie(@Query(API_KEY) apiKey: String): Deferred<MovieResponse>

    @GET(NOW_PLAYING_MOVIE)
    fun getNowPlayingMovie(@Query(API_KEY) apiKey: String): Deferred<MovieResponse>

    @GET(MOVIE_VIDEOS)
    fun getMovieVideos(@Path(MOVIE_ID) movie_id: Int, @Query(API_KEY) apiKey: String): Deferred<TrailerResponse>

    @GET(MOVIE_DETAILS)
    fun getMovieDetails(@Path(MOVIE_ID) movie_id: Int, @Query(API_KEY) apiKey: String): Deferred<MovieFacts>

    @GET(MOVIE)
    fun getMovie(@Path(MOVIE_ID) movie_id: Int, @Query(API_KEY) apiKey: String): Call<Movie>

    @GET(GENRE)
    fun getGenre(@Query(API_KEY) apiKey: String): Deferred<GenreResponse>

    @GET(CREDITS)
    fun getCredits(@Path(MOVIE_ID) movie_id: Int, @Query(API_KEY) apiKey: String): Deferred<MovieCreditsResponse>

    @GET(SIMILAR_MOVIE)
    fun getSimilarMovie(@Path(MOVIE_ID) movie_id: Int, @Query(API_KEY) apiKey: String): Deferred<MovieResponse>

    @GET(MOVIE_IMAGE)
    fun getMovieImages(@Path(MOVIE_ID) movie_id: Int, @Query(API_KEY) apiKey: String): Call<ImageResponse>

    @GET(SEARCH_MOVIE)
    fun getMoviesWithSearch(@Query(API_KEY) apiKey: String, @Query(QUERY) query: String): Call<MovieResponse>

    @GET(WATCHLIST_MOVIE)
    fun getWatchList(@Query(API_KEY) apiKey: String, @Query(SESSION_ID) sessionId: String): Call<MovieResponse>

    @POST(UPDATE_WATCHLIST_MOVIE)
    fun updateWatchList(@Query(API_KEY) apyKey: String, @Query(SESSION_ID) sessionId: String, @Body media: RequestBody): Call<PostResponse>

    @GET(FAVORITES_MOVIE)
    fun getFavoritesList(@Query(API_KEY) apiKey: String, @Query(SESSION_ID) sessionId: String): Call<MovieResponse>

    @POST(UPDATE_FAVORITES_MOVIE)
    fun updateFavoritesList(@Query(API_KEY) apyKey: String, @Query(SESSION_ID) sessionId: String, @Body media: RequestBody): Call<PostResponse>


    @GET(RATED_MOVIE)
    fun getUserRatedMovies(@Query(API_KEY) apiKey: String, @Query(SESSION_ID) sessionId: String): Call<MovieResponse>

    @POST(POST_MOVIE_RATE)
    fun postMovieRate(@Path(MOVIE_ID) movieId: Int, @Query(API_KEY) apiKey: String, @Query(SESSION_ID) sessionId: String, @Body value: RequestBody): Call<PostResponse>

}
