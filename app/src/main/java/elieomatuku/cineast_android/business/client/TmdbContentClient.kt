package elieomatuku.cineast_android.business.client

import android.app.Application
import android.content.res.Resources
import com.google.gson.Gson
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.model.data.*
import elieomatuku.cineast_android.business.api.response.*
import elieomatuku.cineast_android.business.api.MovieApi
import elieomatuku.cineast_android.business.api.PeopleApi
import elieomatuku.cineast_android.business.service.ContentManager
import elieomatuku.cineast_android.business.service.PrefsStore
import elieomatuku.cineast_android.utils.ApiUtils
import elieomatuku.cineast_android.utils.RestUtils
import elieomatuku.cineast_android.utils.ValueStore
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber



class TmdbContentClient (val context: Application, override val resources: Resources): BaseClient {
    companion object {
        const val MOVIE = "movieApi"
    }

    val movieApi: MovieApi by lazy {
        retrofit.create(MovieApi::class.java)
    }

    val peopleApi: PeopleApi by lazy {
        retrofit.create(PeopleApi::class.java)
    }

    private val persistClient: ValueStore by lazy {
        val storeKey = "cineast_prefs"
        PrefsStore(storeKey, context)
    }


    fun getPopularMovies(asyncResponse: AsyncResponse<MovieResponse>) {
        movieApi.getPopularMovie(ContentManager.API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                val success = response?.isSuccessful ?: false

                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }


    fun getUpcomingMovies(asyncResponse: AsyncResponse<MovieResponse>) {
        movieApi.getUpcomingMovies(ContentManager.API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                val success = response?.isSuccessful ?: false

                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }


            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }


    fun getNowPlayingMovies(asyncResponse: AsyncResponse<MovieResponse>) {
        movieApi.getNowPlayingMovie(ContentManager.API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                val success = response?.isSuccessful ?: false

                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun getTopRatedMovies(asyncResponse: AsyncResponse<MovieResponse>) {
        movieApi.getTopRatedMovies(ContentManager.API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                val success = response?.isSuccessful ?: false

                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }


    fun getPopularPeople(asyncResponse: AsyncResponse<PeopleResponse>) {
        peopleApi.getPopularPeople(ContentManager.API_KEY).enqueue(object : Callback<PeopleResponse> {
            override fun onResponse(call: Call<PeopleResponse>?, response: Response<PeopleResponse>?) {
                val success = response?.isSuccessful ?: false

                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<PeopleResponse>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun getGenres(asyncResponse: AsyncResponse<GenreResponse>) {
        movieApi.getGenre(ContentManager.API_KEY).enqueue(object : Callback<GenreResponse> {
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

    fun getMovieVideos(movie: Movie, asyncResponse: AsyncResponse<TrailerResponse>) {
        movieApi.getMovieVideos(movie.id, ContentManager.API_KEY).enqueue(object : Callback<TrailerResponse> {
            override fun onResponse(call: Call<TrailerResponse>?, response: Response<TrailerResponse>?) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }

            }

            override fun onFailure(call: Call<TrailerResponse>?, t: Throwable?) {
                Timber.e("error: $t")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }


    fun getMovieDetails(movie: Movie, asyncResponse: AsyncResponse<MovieDetails>) {
        movieApi.getMovieDetails(movie.id, ContentManager.API_KEY).enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>?, response: Response<MovieDetails>?) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<MovieDetails>?, t: Throwable?) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }


    fun getMovieCredits(movie: Movie, asyncResponse: AsyncResponse<MovieCreditsResponse>) {
        movieApi.getCredits(movie.id, ContentManager.API_KEY).enqueue(object : Callback<MovieCreditsResponse> {
            override fun onResponse(call: Call<MovieCreditsResponse>?, response: Response<MovieCreditsResponse>?) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<MovieCreditsResponse>?, t: Throwable?) {
                Timber.e("error: $t")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun getSimilarMovie(movie: Movie, asyncResponse: AsyncResponse<MovieResponse>) {
        movieApi.getSimilarMovie(movie.id, ContentManager.API_KEY).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {

                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }

            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                Timber.e("error: $t")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun getMovieImages(movieId: Int, asyncResponse: AsyncResponse<ImageResponse>) {
        movieApi.getMovieImages(movieId, ContentManager.API_KEY).enqueue(object : Callback<ImageResponse> {
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
            peopleApi.getPeopleCredits(id, ContentManager.API_KEY).enqueue(object : Callback<PeopleCreditsResponse> {
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
            peopleApi.getPeopleDetails(id, ContentManager.API_KEY).enqueue(object : Callback<PeopleDetails> {
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
        peopleApi.getPeopleImages(personId, ContentManager.API_KEY).enqueue(object : Callback<ImageResponse> {
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
        movieApi.getMovie(movieId, ContentManager.API_KEY).enqueue(object : Callback<Movie> {
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
        movieApi.getMoviesWithSearch(ContentManager.API_KEY, argQuery).enqueue( object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                val success = response?.isSuccessful ?: false
                if (success) {
                    asyncResponse.onSuccess(response?.body())
                } else {
                    asyncResponse.onFail(ApiUtils.throwableToCineastError(response?.errorBody()))
                }
            }


            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                Timber.d( "response: ${t}")
                asyncResponse.onFail(ApiUtils.throwableToCineastError(t))
            }
        })
    }

    fun searchPeople(argQuery: String, asyncResponse: AsyncResponse<PeopleResponse>) {
        peopleApi.getPeopleWithSearch(ContentManager.API_KEY, argQuery).enqueue(object : Callback<PeopleResponse> {
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
            movieApi.getWatchList(RestUtils.API_KEY, it).enqueue(object : Callback<MovieResponse> {
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

            movieApi.updateWatchList(RestUtils.API_KEY, it , getRequestBody(media)).enqueue(
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
            movieApi.getFavoritesList(RestUtils.API_KEY, it).enqueue(object : Callback<MovieResponse> {
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

            movieApi.updateFavoritesList(RestUtils.API_KEY, it , getRequestBody(media)).enqueue(
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

    fun getUserRatedMovies(asyncResponse: AsyncResponse<List<Movie>>) {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            movieApi.getUserRatedMovies(RestUtils.API_KEY, it).enqueue(object : Callback<MovieResponse> {
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