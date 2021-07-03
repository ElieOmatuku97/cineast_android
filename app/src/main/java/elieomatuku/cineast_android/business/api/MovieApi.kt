package elieomatuku.cineast_android.business.api

import elieomatuku.cineast_android.business.api.response.GenreResponse
import elieomatuku.cineast_android.business.api.response.ImageResponse
import elieomatuku.cineast_android.business.api.response.MovieCreditsResponse
import elieomatuku.cineast_android.business.api.response.MovieResponse
import elieomatuku.cineast_android.business.api.response.PostResponse
import elieomatuku.cineast_android.business.api.response.TrailerResponse
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.MovieFacts
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
    fun getTopRatedMovies(): Deferred<MovieResponse>

    @GET(UPCOMING_MOVIE)
    fun getUpcomingMovies(): Deferred<MovieResponse>

    @GET(POPULAR_MOVIE)
    fun getPopularMovie(): Deferred<MovieResponse>

    @GET(NOW_PLAYING_MOVIE)
    fun getNowPlayingMovie(): Deferred<MovieResponse>

    @GET(MOVIE_VIDEOS)
    fun getMovieVideos(@Path(MOVIE_ID) movie_id: Int): Deferred<TrailerResponse>

    @GET(MOVIE_DETAILS)
    fun getMovieDetails(@Path(MOVIE_ID) movie_id: Int): Deferred<MovieFacts>

    @GET(MOVIE)
    fun getMovie(@Path(MOVIE_ID) movie_id: Int): Call<Movie>

    @GET(GENRE)
    fun getGenre(): Deferred<GenreResponse>

    @GET(CREDITS)
    fun getCredits(@Path(MOVIE_ID) movie_id: Int): Deferred<MovieCreditsResponse>

    @GET(SIMILAR_MOVIE)
    fun getSimilarMovie(@Path(MOVIE_ID) movie_id: Int): Deferred<MovieResponse>

    @GET(MOVIE_IMAGE)
    fun getMovieImages(@Path(MOVIE_ID) movie_id: Int): Deferred<ImageResponse>

    @GET(SEARCH_MOVIE)
    fun getMoviesWithSearch(@Query(QUERY) query: String): Call<MovieResponse>

    @GET(WATCHLIST_MOVIE)
    fun getWatchList(@Query(SESSION_ID) sessionId: String): Deferred<MovieResponse>

    @POST(UPDATE_WATCHLIST_MOVIE)
    fun updateWatchList(@Query(SESSION_ID) sessionId: String, @Body media: RequestBody): Call<PostResponse>

    @GET(FAVORITES_MOVIE)
    fun getFavoritesList(@Query(SESSION_ID) sessionId: String): Deferred<MovieResponse>

    @POST(UPDATE_FAVORITES_MOVIE)
    fun updateFavoritesList(@Query(SESSION_ID) sessionId: String, @Body media: RequestBody): Call<PostResponse>

    @GET(RATED_MOVIE)
    fun getUserRatedMovies(@Query(SESSION_ID) sessionId: String): Call<MovieResponse>

    @POST(POST_MOVIE_RATE)
    fun postMovieRate(@Path(MOVIE_ID) movieId: Int, @Query(SESSION_ID) sessionId: String, @Body value: RequestBody): Call<PostResponse>
}
