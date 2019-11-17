package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.rest.RestApi
import elieomatuku.cineast_android.business.service.ContentManager
import elieomatuku.cineast_android.business.model.data.*
import elieomatuku.cineast_android.business.model.response.ImageResponse
import elieomatuku.cineast_android.business.model.response.MovieCreditsResponse
import elieomatuku.cineast_android.business.model.response.MovieResponse
import elieomatuku.cineast_android.business.model.response.TrailerResponse
import elieomatuku.cineast_android.business.service.UserService
import elieomatuku.cineast_android.utils.ApiUtils
import elieomatuku.cineast_android.vu.MovieVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*


class MoviePresenter: BasePresenter<MovieVu>() {
    companion object {
        const val MOVIE_KEY = "movie"
        const val MOVIE_GENRES_KEY = "genres"
        const val SCREEN_NAME_KEY = "screen_name"
        const val MOVIE_DETAILS_KEY = "movie_details"
        const val MOVIE_TRAILERS_KEY = "movie_trailers"
        const val MOVIE_CAST_KEY = "movie_cast_key"
        const val MOVIE_CREW_KEY = "movie_crew_key"
        const val MOVIE_SIMILAR_KEY = "movie_similar_key"
        val TAG = MoviePresenter::class.java.simpleName
    }

    private val restApi: RestApi by App.kodein.instance()
    private val userService: UserService by App.kodein.instance()
    var  movieDetails: MovieDetails? = null
    var trailers: List<Trailer>? = listOf()
    var cast: List<Cast>? = listOf()
    var crew: List<Crew>? = listOf()
    var similarMovies : List<Movie>? = listOf()

    override fun onLink(vu: MovieVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val screenName = args.getString(SCREEN_NAME_KEY)
        val movie: Movie = args.getParcelable(MOVIE_KEY)
        val genres: List <Genre> = args.getParcelableArrayList(MOVIE_GENRES_KEY)


        Timber.d("movie presenter")

        movieDetails = inState?.getParcelable(MOVIE_DETAILS_KEY)
        trailers = inState?.getParcelableArrayList(MOVIE_TRAILERS_KEY)
        cast = inState?.getParcelableArrayList(MOVIE_CAST_KEY)
        crew = inState?.getParcelableArrayList(MOVIE_CREW_KEY)
        similarMovies = inState?.getParcelableArrayList(MOVIE_SIMILAR_KEY)

        vu.moviePresentedPublisher?.onNext(movie)

        if (movieDetails == null || trailers == null || cast == null || crew == null || similarMovies == null) {
            getMovieVideos(movie, screenName, genres)
        } else {
            val movieSummary = MovieSummary (movie, trailers, movieDetails, genres, screenName, cast, crew, similarMovies)
            vu.showMovie(movieSummary)
        }


        rxSubs.add(vu.onProfileClickedPictureObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe ({movieId ->
                    restApi.movie.getMovieImages(movieId, ContentManager.API_KEY).enqueue( object : Callback<ImageResponse>{
                        override fun onResponse(call: Call<ImageResponse>?, response: Response<ImageResponse>?) {
                            val poster = response?.body()?.posters
                            handler.post {
                                vu.goToGallery(poster)
                            }
                        }

                        override fun onFailure(call: Call<ImageResponse>?, t: Throwable?) {
                           Timber.e( "error: $t")
                        }
                    })
                })
        )
    }

    private fun getMovieVideos( movie: Movie, screenName: String?, genres: List<Genre>?) {
        vu?.showLoading()
        restApi.movie.getMovieVideos(movie.id,  ContentManager.API_KEY).enqueue( object : Callback<TrailerResponse> {
            override fun onResponse(call: Call<TrailerResponse>?, response: Response<TrailerResponse>?) {
                trailers = response?.body()?.results
                getMovieDetails(movie, screenName, genres, trailers)

            }
            override fun onFailure(call: Call<TrailerResponse>?, t: Throwable?) {
                Timber.e("error: $t")

                handler.post {
                    vu?.hideLoading()
                    vu?.updateErrorView("")
                }



            }
        })
    }

    private fun getMovieDetails(movie: Movie, screenName: String?, genres: List<Genre>?, trailers: List<Trailer>?) {
        restApi.movie.getMovieDetails(movie.id, ContentManager.API_KEY).enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>?, response: Response<MovieDetails>?) {
                movieDetails = response?.body()
                getMovieCredits(movie, screenName,  movieDetails, genres, trailers)
            }

            override fun onFailure(call: Call<MovieDetails>?, t: Throwable?) {
                Timber.e("error: $t")
            }
        })
    }

    private fun getMovieCredits (movie: Movie, screenName: String?, movieDetails: MovieDetails?, genres: List<Genre>? ,trailers: List<Trailer>?) {
        restApi.movie.getCredits( movie.id, ContentManager.API_KEY).enqueue(object : Callback<MovieCreditsResponse> {
            override fun onResponse(call: Call<MovieCreditsResponse>?, response: Response<MovieCreditsResponse>?) {
                cast = response?.body()?.cast
                crew = response?.body()?.crew
                getSimilarMovies(movie, screenName, genres, movieDetails, trailers, cast, crew)
            }
            override fun onFailure(call: Call<MovieCreditsResponse>?, t: Throwable?) {
                Timber.e("error: $t")
            }
        })
    }

    private fun getSimilarMovies(movie: Movie, screenName: String?, genres: List<Genre>?, movieDetails: MovieDetails?, trailers: List<Trailer>?, cast: List<Cast>?, crew: List<Crew>?) {
        restApi.movie.getSimilarMovie( movie.id, ContentManager.API_KEY).enqueue(object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                similarMovies = response?.body()?.results
                val movieSummary = MovieSummary(movie, trailers, movieDetails, genres, screenName, cast, crew, similarMovies)

                if (!userService.isLoggedIn()) {
                    handler.post {
                        vu?.hideLoading()
                        vu?.showMovie(movieSummary)
                    }
                } else {
                    movieSummary.movie?.let {
                        checkIfMovieInWatchList(movieSummary)
                    }

                }
            }
            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                Timber.e("error: $t")
                vu?.updateErrorView(ApiUtils.throwableToCineastError(t).status_message)
            }
        })
    }


    private fun checkIfMovieInWatchList(movieSummary: MovieSummary)  {
        userService.getWatchList(object: AsyncResponse<List<Movie>> {
            override fun onSuccess(result: List<Movie>?) {
                Timber.d("watch list result: ${result} \n movie selected: ${movieSummary.movie}")
                val isInWatchList = result?.let {
                    it.contains(movieSummary.movie)
                } ?: false

                vu?.watchListCheckPublisher?.onNext(isInWatchList)
                checkIfMovieInFavoriteList(movieSummary)
            }

            override fun onFail(error: CineastError) {
                Timber.e("error from fetching watch list: $error")
            }
        })
    }


    private fun checkIfMovieInFavoriteList(movieSummary: MovieSummary) {
        userService.getFavoriteList(object: AsyncResponse<List<Movie>> {
            override fun onSuccess(result: List<Movie>?) {
                Timber.d("favorite list result: ${result} \n movie selected: ${movieSummary.movie}")
                val isInFavoriteList = result?.let {
                    it.contains(movieSummary.movie)
                } ?: false

                handler.post {
                    vu?.hideLoading()
                    vu?.favoriteListCheckPublisher?.onNext(isInFavoriteList)
                    vu?.showMovie(movieSummary)
                    Timber.d("isInFavoriteList: $isInFavoriteList")
                }
            }

            override fun onFail(error: CineastError) {
                Timber.e("error from fetching favorite list: $error")
                vu?.updateErrorView(error.status_message)
            }
        })
    }

    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)

        movieDetails?.let {
            outState.putParcelable(MOVIE_DETAILS_KEY, it)
        }

        trailers?.let {
            outState.putParcelableArrayList(MOVIE_TRAILERS_KEY, it as ArrayList<out Parcelable>)
        }

        cast?.let{
            outState.putParcelableArrayList(MOVIE_CAST_KEY, it as ArrayList<out Parcelable>)
        }

        crew?.let {
            outState.putParcelableArrayList(MOVIE_CREW_KEY, it as ArrayList<out Parcelable>)
        }

        similarMovies?.let {
            outState.putParcelableArrayList(MOVIE_SIMILAR_KEY, it as ArrayList<out Parcelable>)
        }
    }
}