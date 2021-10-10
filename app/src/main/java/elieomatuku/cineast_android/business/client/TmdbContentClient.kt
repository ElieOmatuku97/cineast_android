package elieomatuku.cineast_android.business.client

import android.content.res.Resources
import com.google.gson.Gson
import elieomatuku.cineast_android.business.api.MovieApi
import elieomatuku.cineast_android.business.api.response.MovieResponse
import elieomatuku.cineast_android.business.api.response.PostResponse
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.data.PrefManager
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.remote.request.FavouritesMediaRequest
import elieomatuku.cineast_android.remote.request.RateRequest
import elieomatuku.cineast_android.remote.request.WatchListMediaRequest
import elieomatuku.cineast_android.utils.ApiUtils
import elieomatuku.cineast_android.utils.RestUtils
import io.flatcircle.coroutinehelper.ApiResult
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class TmdbContentClient(
    override val resources: Resources,
    override val persistClient: PrefManager,
    override val interceptor: Interceptor? = null
) : BaseClient {
    companion object {
        const val MOVIE = "movieApi"
    }

    private val movieApi: MovieApi by lazy {
        retrofit.create(MovieApi::class.java)
    }

    suspend fun getWatchList(): ApiResult<MovieResponse> {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            return try {
                val movieResponse = movieApi.getWatchList(it).await()
                Timber.i("getWatchList was succesful: $movieResponse")
                ApiResult.success(movieResponse)
            } catch (e: Exception) {
                Timber.w("getWatchList failed with $e")
                ApiResult.fail(e)
            }
        }

        Timber.w("getWatchList failed, session id null.")
        return ApiResult.notImplemented()
    }

    fun removeMovieFromWatchList(movie: Movie) {
        updateWatchList(movie, false)
    }

    private fun updateWatchList(movie: Movie, watchList: Boolean) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            val media = WatchListMediaRequest(MOVIE, movie.id, watchList)

            movieApi.updateWatchList(it, getRequestBody(media)).enqueue(
                object : Callback<PostResponse> {
                    override fun onResponse(
                        call: Call<PostResponse>,
                        response: Response<PostResponse>
                    ) {
                        Timber.d("response: ${response.body()}")
                    }

                    override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                        Timber.e("error add movieApi to watch list: $t")
                    }
                }
            )
        }
    }

    suspend fun getFavoriteList(): ApiResult<MovieResponse> {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            return try {
                val movieResponse = movieApi.getFavoritesList(it).await()
                Timber.i("getFavoriteList was succesful: $movieResponse")
                ApiResult.success(movieResponse)
            } catch (e: Exception) {
                Timber.w("getFavoriteList failed with $e")
                ApiResult.fail(e)
            }
        }

        Timber.w("getFavoriteList failed, session id null.")
        return ApiResult.notImplemented()
    }

    fun removeMovieFromFavoriteList(movie: Movie) {
        updateFavoriteList(movie, false)
    }

    private fun updateFavoriteList(movie: Movie, favorite: Boolean) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            val media = FavouritesMediaRequest(MOVIE, movie.id, favorite)

            movieApi.updateFavoritesList(it, getRequestBody(media)).enqueue(
                object : Callback<PostResponse> {
                    override fun onResponse(
                        call: Call<PostResponse>,
                        response: Response<PostResponse>
                    ) {
                        Timber.d("response: ${response.body()}")
                    }

                    override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                        Timber.e("error add movieApi to watch list: $t")
                    }
                }
            )
        }
    }

    fun postMovieRate(movie: Movie, value: Double) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            val rate = RateRequest(value)

            movieApi.postMovieRate(movie.id, it, getRequestBody(rate)).enqueue(
                object : Callback<PostResponse> {
                    override fun onResponse(
                        call: Call<PostResponse>,
                        response: Response<PostResponse>
                    ) {
                        Timber.d("response: ${response.body()}")
                    }

                    override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                        Timber.e("error post rate : $t")
                    }
                }
            )
        }
    }

    fun getUserRatedMovies(asyncResponse: AsyncResponse<List<Movie>>) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            movieApi.getUserRatedMovies(it).enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    val success = response.isSuccessful
                    if (success) {
                        response.body()?.results?.let {
                            asyncResponse.onSuccess(it)
                        }
                    } else {
                        asyncResponse.onFail(ApiUtils.throwableToCineastError(response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
                }
            })
        }
    }

    private fun <T> getRequestBody(item: T): RequestBody {
        val mediaType = "application/json".toMediaType()
        return RequestBody.create(mediaType, toJson(item))
    }

    private fun <T> toJson(item: T): String {
        val gson = Gson()

        return gson.toJson(item)
    }
}
