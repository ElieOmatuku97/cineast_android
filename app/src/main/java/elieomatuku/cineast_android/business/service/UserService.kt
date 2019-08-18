package elieomatuku.cineast_android.business.service

import android.app.Application
import com.google.gson.Gson
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.MoshiSerializer
import elieomatuku.cineast_android.business.client.Serializer
import elieomatuku.cineast_android.business.model.data.*
import elieomatuku.cineast_android.business.model.response.PostResponse
import elieomatuku.cineast_android.business.model.response.MovieResponse
import elieomatuku.cineast_android.business.rest.MovieApi
import elieomatuku.cineast_android.business.rest.RestApi
import elieomatuku.cineast_android.utils.RestUtils
import elieomatuku.cineast_android.utils.ValueStore
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import okhttp3.RequestBody




class UserService (private val restApi: RestApi, private val movieApi: MovieApi, private val application: Application) {
    private val MOVIE = "movie"

    private val persistClient: ValueStore by lazy {
        val storeKey = "cineast_prefs"
        PrefsStore(storeKey, application)
    }

    private val accountSerializer: Serializer<Account> by lazy {
        MoshiSerializer<Account>(Account::class.java)
    }

    fun getAccessToken(asyncResponse: AsyncResponse<AccessToken>) {
        restApi.authentication.getAccessToken(RestUtils.API_KEY).enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>?) {
                Timber.d("AccessToken: $response")

                response?.let {
                    it.body()?.let {
                        it.request_token?.let {
                            persistClient.set(RestUtils.REQUEST_TOKEN_KEY, it)
                        }
                    }
                    asyncResponse.onSuccess(it.body())
                }
            }

            override fun onFailure(call: Call<AccessToken>, throwable: Throwable) {
                asyncResponse.onFail(throwable.toString())
            }
        })
    }

    fun getSession(requestToken: String?, asyncResponse: AsyncResponse<String>) {
        restApi.authentication.getSession(RestUtils.API_KEY, requestToken).enqueue(object : Callback<Session> {
            override fun onResponse(call: Call<Session>, response: Response<Session>) {
                response.body()?.session_id?.let {
                    persistClient.set(RestUtils.SESSION_ID_KEY, it)
                    asyncResponse.onSuccess(it)
                    setAccount(it)
                }
            }

            override fun onFailure(call: Call<Session>, t: Throwable) {
                Timber.d("session error: $t")
            }
        })
    }

    fun setAccount(sessionId: String?) {
        sessionId?.let {
            restApi.authentication.getAccount(RestUtils.API_KEY, it).enqueue(object : Callback<Account> {
                override fun onResponse(call: Call<Account>, response: Response<Account>) {
                    Timber.d("account: ${response.body()}")

                    response.body()?.let {
                        persistClient.set(RestUtils.ACCOUNT_ID_KEY, accountSerializer.toJson(it))
                    }

                }

                override fun onFailure(call: Call<Account>, t: Throwable) {
                    Timber.e("t: $t")
                }
            })
        }
    }

    fun getAccount(): Account? {
        return persistClient.get(RestUtils.ACCOUNT_ID_KEY, null)?.let {
            accountSerializer.fromJson(it)
        }
    }

    fun getRequestToken(): String? {
        return persistClient.get(RestUtils.REQUEST_TOKEN_KEY, null)
    }

    fun logout() {
        persistClient.remove(RestUtils.SESSION_ID_KEY)
        persistClient.remove(RestUtils.REQUEST_TOKEN_KEY)
        persistClient.remove(RestUtils.ACCOUNT_ID_KEY)

    }

    fun isLoggedIn(): Boolean {
        return !persistClient.get(RestUtils.SESSION_ID_KEY, null).isNullOrEmpty()
    }

    fun getWatchList(asyncResponse: AsyncResponse<List<Movie>>) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            movieApi.getWatchList(RestUtils.API_KEY, it).enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    response.body()?.results?.let {
                        asyncResponse.onSuccess(it)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    asyncResponse.onFail(t.toString())
                }
            })
        }
    }

    fun addMovieToWatchList(movie: Movie) {
        updateWatchList(movie, true)

    }

    fun removeMovieFromWatchList(movie: Movie) {
        updateWatchList(movie, false)
    }

    private fun updateWatchList(movie: Movie, watchList: Boolean) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            val media = WatchListMedia(MOVIE, movie.id, watchList)

            movieApi.updateWatchList(RestUtils.API_KEY, it , getRequestBody(media)).enqueue(
                    object : Callback<PostResponse> {
                        override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                            Timber.d("response: ${response.body()}")
                        }

                        override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                            Timber.e("error add movie to watch list: $t")
                        }
                    }
            )
        }
    }

    fun getFavoriteList(asyncResponse: AsyncResponse<List<Movie>>) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            movieApi.getFavoritesList(RestUtils.API_KEY, it).enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    response.body()?.results?.let {
                        asyncResponse.onSuccess(it)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    asyncResponse.onFail(t.toString())
                }
            })
        }
    }

    fun addMovieToFavoriteList(movie: Movie) {
        updateFavoriteList(movie, true)

    }

    fun removeMovieFromFavoriteList(movie: Movie) {
        updateFavoriteList(movie, false)
    }

    private fun updateFavoriteList(movie: Movie, favorite: Boolean) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            val media = FavoriteListMedia(MOVIE, movie.id, favorite)

            movieApi.updateFavoritesList(RestUtils.API_KEY, it , getRequestBody(media)).enqueue(
                    object : Callback<PostResponse> {
                        override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                            Timber.d("response: ${response.body()}")
                        }

                        override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                            Timber.e("error add movie to watch list: $t")
                        }
                    }
            )
        }
    }

    fun getUserRatedMovies(asyncResponse: AsyncResponse<List<Movie>>) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            movieApi.getUserRatedMovies(RestUtils.API_KEY, it).enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    response.body()?.results?.let {
                        asyncResponse.onSuccess(it)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    asyncResponse.onFail(t.toString())
                }
            })
        }
    }

    fun postMovieRate(movie: Movie, value: Double) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            val rate = Rate(value)

            movieApi.postMovieRate(movie.id, RestUtils.API_KEY, it , getRequestBody(rate)).enqueue(
                    object : Callback<PostResponse> {
                        override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                            Timber.d("response: ${response.body()}")
                        }

                        override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                            Timber.e("error post rate : $t")
                        }
                    }
            )
        }
    }

    private fun <T> getRequestBody(item: T): RequestBody {
        val mediaType = MediaType.parse("application/json")
        return RequestBody.create(mediaType, toJson(item))
    }

    private fun <T> toJson(item: T): String {
        val gson = Gson()
        val jsonString: String = gson.toJson(item)

        return jsonString
    }
}