package elieomatuku.cineast_android.business.api

import elieomatuku.cineast_android.business.api.response.MovieResponse
import elieomatuku.cineast_android.business.api.response.PostResponse
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

        const val SEARCH_MOVIE = "search/movie"
        const val WATCHLIST_MOVIE = "account/{$ACCOUNT_ID}/watchlist/movies"
        const val UPDATE_WATCHLIST_MOVIE = "account/{$ACCOUNT_ID}/watchlist"
        const val FAVORITES_MOVIE = "account/{account_id}/favorite/movies"
        const val UPDATE_FAVORITES_MOVIE = "account/{$ACCOUNT_ID}/favorite"
        const val RATED_MOVIE = "account/{$ACCOUNT_ID}/rated/movies"
        const val POST_MOVIE_RATE = "movie/{$MOVIE_ID}/rating"
    }

    @GET(SEARCH_MOVIE)
    fun getMoviesWithSearch(@Query(QUERY) query: String): Call<MovieResponse>

    @GET(WATCHLIST_MOVIE)
    fun getWatchList(@Query(SESSION_ID) sessionId: String): Deferred<MovieResponse>

    @POST(UPDATE_WATCHLIST_MOVIE)
    fun updateWatchList(
        @Query(SESSION_ID) sessionId: String,
        @Body media: RequestBody
    ): Call<PostResponse>

    @GET(FAVORITES_MOVIE)
    fun getFavoritesList(@Query(SESSION_ID) sessionId: String): Deferred<MovieResponse>

    @POST(UPDATE_FAVORITES_MOVIE)
    fun updateFavoritesList(
        @Query(SESSION_ID) sessionId: String,
        @Body media: RequestBody
    ): Call<PostResponse>

    @GET(RATED_MOVIE)
    fun getUserRatedMovies(@Query(SESSION_ID) sessionId: String): Call<MovieResponse>

    @POST(POST_MOVIE_RATE)
    fun postMovieRate(
        @Path(MOVIE_ID) movieId: Int,
        @Query(SESSION_ID) sessionId: String,
        @Body value: RequestBody
    ): Call<PostResponse>
}
