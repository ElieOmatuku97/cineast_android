package elieomatuku.cineast_android.business.api

import elieomatuku.cineast_android.business.api.response.PostResponse
import elieomatuku.cineast_android.model.data.Movie
import elieomatuku.cineast_android.model.data.MovieDetails
import elieomatuku.cineast_android.business.api.response.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface MovieApi {
    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") apyKey: String): Call<MovieResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("api_key") apiKey: String): Call<MovieResponse>

    @GET("movie/popular")
    fun getPopularMovie(@Query("api_key") apiKey: String): Call<MovieResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(@Query("api_key") apiKey: String): Call<MovieResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovie(@Query("api_key") apiKey: String): Call<MovieResponse>

    @GET("movie/{movie_id}/videos")
    fun getMovieVideos(@Path("movie_id") movie_id: Int, @Query("api_key") apiKey: String): Call<TrailerResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movie_id: Int, @Query("api_key") apiKey: String): Call<MovieDetails>

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movie_id: Int, @Query("api_key") apiKey: String): Call<Movie>

    @GET("genre/movie/list")
    fun getGenre(@Query("api_key") apiKey: String): Call<GenreResponse>

    @GET("movie/{movie_id}/credits")
    fun getCredits(@Path("movie_id") movie_id: Int, @Query("api_key") apiKey: String): Call<MovieCreditsResponse>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovie(@Path("movie_id") movie_id: Int, @Query("api_key") apiKey: String): Call<MovieResponse>

    @GET("movie/{movie_id}/images")
    fun getMovieImages(@Path("movie_id") movie_id: Int, @Query("api_key") apiKey: String): Call<ImageResponse>

    @GET("search/movie")
    fun getMoviesWithSearch(@Query("api_key") apiKey: String, @Query("query") query: String): Call<MovieResponse>

    @GET("account/{account_id}/watchlist/movies")
    fun getWatchList(@Query("api_key") apiKey: String, @Query("session_id") sessionId: String): Call<MovieResponse>

    @POST("account/{account_id}/watchlist")
    fun updateWatchList(@Query("api_key") apyKey: String,
                        @Query("session_id") sessionId: String,
                        @Body media: RequestBody): Call<PostResponse>

    @GET("account/{account_id}/favorite/movies")
    fun getFavoritesList(@Query("api_key") apiKey: String, @Query("session_id") sessionId: String): Call<MovieResponse>

    @POST("account/{account_id}/favorite")
    fun updateFavoritesList(@Query("api_key") apyKey: String,
                            @Query("session_id") sessionId: String,
                            @Body media: RequestBody): Call<PostResponse>


    @GET("account/{account_id}/rated/movies")
    fun getUserRatedMovies(@Query("api_key") apiKey: String, @Query("session_id") sessionId: String): Call<MovieResponse>


    @POST("movie/{movie_id}/rating")
    fun postMovieRate(@Path("movie_id") movieId: Int,
                      @Query("api_key") apiKey: String,
                      @Query("session_id") sessionId: String,
                      @Body value: RequestBody): Call<PostResponse>

}
