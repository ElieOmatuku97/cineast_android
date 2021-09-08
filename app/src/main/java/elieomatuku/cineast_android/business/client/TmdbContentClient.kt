package elieomatuku.cineast_android.business.client

import android.content.res.Resources
import com.google.gson.Gson
import elieomatuku.cineast_android.business.api.MovieApi
import elieomatuku.cineast_android.business.api.PeopleApi
import elieomatuku.cineast_android.business.api.response.GenreResponse
import elieomatuku.cineast_android.business.api.response.ImageResponse
import elieomatuku.cineast_android.business.api.response.MovieCreditsResponse
import elieomatuku.cineast_android.business.api.response.MovieResponse
import elieomatuku.cineast_android.business.api.response.PeopleCreditsResponse
import elieomatuku.cineast_android.business.api.response.PersonalityResponse
import elieomatuku.cineast_android.business.api.response.PostResponse
import elieomatuku.cineast_android.business.api.response.TrailerResponse
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.domain.ValueStore
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.MovieFacts
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.domain.model.PersonDetails
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
    override val persistClient: ValueStore,
    override val interceptor: Interceptor? = null
) : BaseClient {
    companion object {
        const val MOVIE = "movieApi"
    }

    private val movieApi: MovieApi by lazy {
        retrofit.create(MovieApi::class.java)
    }

    private val peopleApi: PeopleApi by lazy {
        retrofit.create(PeopleApi::class.java)
    }

    suspend fun getPopularMovies(): MovieResponse? {
        return try {
            val results = movieApi.getPopularMovie().await()
            Timber.i("get Popular Movies was succesful: $results")
            results
        } catch (e: Exception) {
            Timber.w("get Popular Movies failed with $e")
            null
        }
    }

    suspend fun getUpcomingMovies(): MovieResponse? {
        return try {
            val results = movieApi.getUpcomingMovies().await()
            Timber.i("get Upcoming Movies was succesful: $results")
            results
        } catch (e: Exception) {
            Timber.w("get Upcoming Movies failed with $e")
            null
        }
    }

    suspend fun getNowPlayingMovies(): MovieResponse? {
        return try {
            val results = movieApi.getNowPlayingMovie().await()
            Timber.i("get Upcoming Movies was succesful: $results")
            results
        } catch (e: Exception) {
            Timber.w("get Upcoming Movies failed with $e")
            null
        }
    }

    suspend fun getTopRatedMovies(): MovieResponse? {
        return try {
            val results = movieApi.getTopRatedMovies().await()
            Timber.i("get Upcoming Movies was succesful: $results")
            results
        } catch (e: Exception) {
            Timber.w("get Upcoming Movies failed with $e")
            null
        }
    }

    suspend fun getPersonalities(): PersonalityResponse? {
        return try {
            val results = peopleApi.getPersonalities().await()
            Timber.i("get Popular People was succesful: $results")
            results
        } catch (e: Exception) {
            Timber.w("get Popular People failed with $e")
            null
        }
    }

    suspend fun getGenres(): ApiResult<GenreResponse> {
        return try {
            val value = movieApi.getGenre().await()
            ApiResult.success(value)
        } catch (e: Exception) {
            ApiResult.fail(e)
        }
    }

    suspend fun getMovieVideos(movie: Movie): ApiResult<TrailerResponse> {
        return try {
            val value = movieApi.getMovieVideos(movie.id).await()
            Timber.i("get Movie Videos was succesful: $value")
            ApiResult.success(value)
        } catch (e: Exception) {
            Timber.w("get Movie Videos failed with $e")
            ApiResult.fail(e)
        }
    }

    suspend fun getMovieFacts(movie: Movie): ApiResult<MovieFacts> {
        return try {
            val value = movieApi.getMovieDetails(movie.id).await()
            Timber.i("get Movie Details was succesful: $value")
            ApiResult.success(value)
        } catch (e: Exception) {
            Timber.w("get Movie Details failed with $e")
            ApiResult.fail(e)
        }
    }

    suspend fun getMovieCredits(movie: Movie): ApiResult<MovieCreditsResponse> {
        return try {
            val value = movieApi.getCredits(movie.id).await()
            Timber.i("get Movie Credits was succesful: $value")
            ApiResult.success(value)
        } catch (e: Exception) {
            Timber.w("get Movie Credits failed with $e")
            ApiResult.fail(e)
        }
    }

    suspend fun getSimilarMovie(movie: Movie): ApiResult<MovieResponse> {
        return try {
            val value = movieApi.getSimilarMovie(movie.id).await()
            Timber.i("get Similar Movie was succesful: $value")
            ApiResult.success(value)
        } catch (e: Exception) {
            Timber.w("get Similar Movie failed with $e")
            ApiResult.fail(e)
        }
    }

    suspend fun getMovieImages(movieId: Int): ApiResult<ImageResponse> {
        return try {
            val response = movieApi.getMovieImages(movieId).await()
            Timber.i("getMovieImage was succesful: $response")
            ApiResult.success(response)
        } catch (e: Exception) {
            Timber.w("getMovieImage failed with $e")
            ApiResult.fail(e)
        }
    }

    fun getPeopleMovies(person: Person, asyncResponse: AsyncResponse<PeopleCreditsResponse>) {
        val id = person.id
        if (id != null) {
            peopleApi.getPeopleCredits(id).enqueue(object : Callback<PeopleCreditsResponse> {
                override fun onResponse(
                    call: Call<PeopleCreditsResponse>?,
                    response: Response<PeopleCreditsResponse>?
                ) {

                    val success = response?.isSuccessful ?: false
                    if (success) {
                        asyncResponse.onSuccess(response?.body())
                    } else {
                        asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                    }
                }

                override fun onFailure(call: Call<PeopleCreditsResponse>?, t: Throwable?) {
                    Timber.d("error: $t")
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
                }
            })
        }
    }

    fun getPeopleDetails(person: Person, asyncResponse: AsyncResponse<PersonDetails>) {
        val id = person.id
        if (id != null) {
            peopleApi.getPeopleDetails(id).enqueue(object : Callback<PersonDetails> {
                override fun onResponse(
                    call: Call<PersonDetails>?,
                    response: Response<PersonDetails>?
                ) {
                    val success = response?.isSuccessful ?: false
                    if (success) {
                        asyncResponse.onSuccess(response?.body())
                    } else {
                        asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                    }
                }

                override fun onFailure(call: Call<PersonDetails>?, t: Throwable?) {
                    Timber.e("error: $t")
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
                }
            })
        }
    }

    fun getPeopleImages(personId: Int, asyncResponse: AsyncResponse<ImageResponse>) {
        peopleApi.getPeopleImages(personId).enqueue(object : Callback<ImageResponse> {
            override fun onResponse(
                call: Call<ImageResponse>?,
                response: Response<ImageResponse>?
            ) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<ImageResponse>?, t: Throwable?) {
                Timber.d("error: $t")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun getMovie(movieId: Int, asyncResponse: AsyncResponse<Movie>) {
        movieApi.getMovie(movieId).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>?, response: Response<Movie>?) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<Movie>?, t: Throwable?) {
                Timber.d("error: $t")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun searchMovies(argQuery: String, asyncResponse: AsyncResponse<MovieResponse>) {
        movieApi.getMoviesWithSearch(argQuery).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>?,
                response: Response<MovieResponse>?
            ) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                Timber.d("response: $t")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun searchPeople(argQuery: String, asyncResponse: AsyncResponse<PersonalityResponse>) {
        peopleApi.getPeopleWithSearch(argQuery).enqueue(object : Callback<PersonalityResponse> {
            override fun onResponse(
                call: Call<PersonalityResponse>?,
                response: Response<PersonalityResponse>?
            ) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<PersonalityResponse>?, t: Throwable?) {
                Timber.d("response: $t")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
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

    fun addMovieToWatchList(movie: Movie) {
        updateWatchList(movie, true)
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

    fun addMovieToFavoriteList(movie: Movie) {
        updateFavoriteList(movie, true)
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
