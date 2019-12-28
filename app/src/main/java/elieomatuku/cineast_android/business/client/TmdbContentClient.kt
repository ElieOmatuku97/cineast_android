package elieomatuku.cineast_android.business.client


import android.content.res.Resources
import com.google.gson.Gson
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.model.data.*
import elieomatuku.cineast_android.business.api.response.*
import elieomatuku.cineast_android.business.api.MovieApi
import elieomatuku.cineast_android.business.api.PeopleApi
import elieomatuku.cineast_android.utils.ApiUtils
import elieomatuku.cineast_android.utils.RestUtils
import elieomatuku.cineast_android.ValueStore
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class TmdbContentClient(override val resources: Resources) : BaseClient {
    companion object {
        const val MOVIE = "movieApi"
        val API_KEY = RestUtils.API_KEY
    }

    val movieApi: MovieApi by lazy {
        retrofit.create(MovieApi::class.java)
    }

    val peopleApi: PeopleApi by lazy {
        retrofit.create(PeopleApi::class.java)
    }

    override val persistClient: ValueStore by App.kodein.instance()

    suspend fun getPopularMovies(): MovieResponse? {
        return try {
            val results = movieApi.getPopularMovie(API_KEY).await()
            Timber.i("get Popular Movies was succesful: $results")
            results
        } catch (e: Exception) {
            Timber.w("get Popular Movies failed with $e")
            null
        }
    }


    suspend fun getUpcomingMovies(): MovieResponse? {
        return try {
            val results = movieApi.getUpcomingMovies(API_KEY).await()
            Timber.i("get Upcoming Movies was succesful: $results")
            results
        } catch (e: Exception) {
            Timber.w("get Upcoming Movies failed with $e")
            null
        }
    }

    suspend fun getNowPlayingMovies(): MovieResponse? {
        return try {
            val results = movieApi.getNowPlayingMovie(API_KEY).await()
            Timber.i("get Upcoming Movies was succesful: $results")
            results
        } catch (e: Exception) {
            Timber.w("get Upcoming Movies failed with $e")
            null
        }
    }

    suspend fun getTopRatedMovies(): MovieResponse? {
        return try {
            val results = movieApi.getTopRatedMovies(API_KEY).await()
            Timber.i("get Upcoming Movies was succesful: $results")
            results
        } catch (e: Exception) {
            Timber.w("get Upcoming Movies failed with $e")
            null

        }
    }

    suspend fun getPopularPeople(): PeopleResponse? {
        return try {
            val results = peopleApi.getPopularPeople(API_KEY).await()
            Timber.i("get Popular People was succesful: $results")
            results
        } catch (e: Exception) {
            Timber.w("get Popular People failed with $e")
            null
        }
    }

    fun getGenres(asyncResponse: AsyncResponse<GenreResponse>) {
        movieApi.getGenre(API_KEY).enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>?, response: Response<GenreResponse>?) {
                val success = response?.isSuccessful ?: false

                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }

            }

            override fun onFailure(call: Call<GenreResponse>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    suspend fun getMovieVideos(movie: Movie): TrailerResponse? {
        return try {
            val results = movieApi.getMovieVideos(movie.id, API_KEY).await()
            Timber.i("get Movie Videos was succesful: $results")
            results
        } catch (e: Exception) {
            Timber.w("get Movie Videos failed with $e")
            null
        }
    }


    suspend fun getMovieDetails(movie: Movie): MovieDetails? {
        return try {
            val results = movieApi.getMovieDetails(movie.id, API_KEY).await()
            Timber.i("get Movie Details was succesful: $results")
            results

        } catch (e: Exception) {
            Timber.w("get Movie Details failed with $e")
            null
        }
    }


    suspend fun getMovieCredits(movie: Movie) : MovieCreditsResponse? {
        return try {
            val results = movieApi.getCredits(movie.id, API_KEY).await()
            Timber.i("get Movie Credits was succesful: $results")
            results

        } catch (e: Exception) {
            Timber.w("get Movie Credits failed with $e")
            null
        }
    }

    suspend fun getSimilarMovie(movie: Movie): MovieResponse? {
        return try {
            val results = movieApi.getSimilarMovie(movie.id, API_KEY).await()
            Timber.i("get Similar Movie was succesful: $results")
            results

        } catch (e: Exception) {
            Timber.w("get Similar Movie failed with $e")
            null
        }
    }

    fun getMovieImages(movieId: Int, asyncResponse: AsyncResponse<ImageResponse>) {
        movieApi.getMovieImages(movieId, API_KEY).enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>?, response: Response<ImageResponse>?) {

                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<ImageResponse>?, t: Throwable?) {
                Timber.e("error: $t")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun getPeopleMovies(person: Person, asyncResponse: AsyncResponse<PeopleCreditsResponse>) {
        val id = person.id
        if (id != null) {
            peopleApi.getPeopleCredits(id, API_KEY).enqueue(object : Callback<PeopleCreditsResponse> {
                override fun onResponse(call: Call<PeopleCreditsResponse>?, response: Response<PeopleCreditsResponse>?) {

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

    fun getPeopleDetails(person: Person, asyncResponse: AsyncResponse<PeopleDetails>) {
        val id = person.id
        if (id != null) {
            peopleApi.getPeopleDetails(id, API_KEY).enqueue(object : Callback<PeopleDetails> {
                override fun onResponse(call: Call<PeopleDetails>?, response: Response<PeopleDetails>?) {
                    val success = response?.isSuccessful ?: false
                    if (success) {
                        asyncResponse.onSuccess(response?.body())
                    } else {
                        asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                    }
                }

                override fun onFailure(call: Call<PeopleDetails>?, t: Throwable?) {
                    Timber.e("error: $t")
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
                }
            })
        }
    }

    fun getPeopleImages(personId: Int, asyncResponse: AsyncResponse<ImageResponse>) {
        peopleApi.getPeopleImages(personId, API_KEY).enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>?, response: Response<ImageResponse>?) {
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
        movieApi.getMovie(movieId, API_KEY).enqueue(object : Callback<Movie> {
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
        movieApi.getMoviesWithSearch(API_KEY, argQuery).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }


            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                Timber.d("response: ${t}")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun searchPeople(argQuery: String, asyncResponse: AsyncResponse<PeopleResponse>) {
        peopleApi.getPeopleWithSearch(API_KEY, argQuery).enqueue(object : Callback<PeopleResponse> {
            override fun onResponse(call: Call<PeopleResponse>?, response: Response<PeopleResponse>?) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }


            override fun onFailure(call: Call<PeopleResponse>?, t: Throwable?) {
                Timber.d("response: ${t}")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }


    fun getWatchList(asyncResponse: AsyncResponse<List<Movie>>) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            movieApi.getWatchList(API_KEY, it).enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    val success = response?.isSuccessful ?: false
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

    fun addMovieToWatchList(movie: Movie) {
        updateWatchList(movie, true)

    }

    fun removeMovieFromWatchList(movie: Movie) {
        updateWatchList(movie, false)
    }

    private fun updateWatchList(movie: Movie, watchList: Boolean) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            val media = WatchListMedia(MOVIE, movie.id, watchList)

            movieApi.updateWatchList(API_KEY, it, getRequestBody(media)).enqueue(
                    object : Callback<PostResponse> {
                        override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                            Timber.d("response: ${response.body()}")
                        }

                        override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                            Timber.e("error add movieApi to watch list: $t")
                        }
                    }
            )
        }
    }


    fun getFavoriteList(asyncResponse: AsyncResponse<List<Movie>>) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            movieApi.getFavoritesList(API_KEY, it).enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    val success = response?.isSuccessful ?: false
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

    fun addMovieToFavoriteList(movie: Movie) {
        updateFavoriteList(movie, true)

    }

    fun removeMovieFromFavoriteList(movie: Movie) {
        updateFavoriteList(movie, false)
    }


    private fun updateFavoriteList(movie: Movie, favorite: Boolean) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            val media = FavoriteListMedia(MOVIE, movie.id, favorite)

            movieApi.updateFavoritesList(API_KEY, it, getRequestBody(media)).enqueue(
                    object : Callback<PostResponse> {
                        override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
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
            val rate = Rate(value)

            movieApi.postMovieRate(movie.id, API_KEY, it, getRequestBody(rate)).enqueue(
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

    fun getUserRatedMovies(asyncResponse: AsyncResponse<List<Movie>>) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            movieApi.getUserRatedMovies(API_KEY, it).enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    val success = response?.isSuccessful ?: false
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
        val jsonString: String = gson.toJson(item)

        return jsonString
    }
}