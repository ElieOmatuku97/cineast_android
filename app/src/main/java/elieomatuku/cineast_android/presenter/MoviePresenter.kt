package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.business.service.RestService
import elieomatuku.cineast_android.business.business.service.DiscoverService
import elieomatuku.cineast_android.business.business.model.data.*
import elieomatuku.cineast_android.business.business.model.response.ImageResponse
import elieomatuku.cineast_android.business.business.model.response.MovieCreditsResponse
import elieomatuku.cineast_android.business.business.model.response.MovieResponse
import elieomatuku.cineast_android.business.business.model.response.TrailerResponse
import elieomatuku.cineast_android.vu.MovieVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
    private val restService: RestService by App.kodein.instance()
    var  movieDetails: MovieDetails? = null
    var trailers: List<Trailer>? = listOf()
    var cast: List<Cast>? = listOf()
    var crew: List<Crew>? = listOf()
    var similarMovies : List<Movie>? = listOf()

    override fun onLink(vu: MovieVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val screenName = args.getString(MoviePresenter.SCREEN_NAME_KEY)
        val movie: Movie = args.getParcelable(MoviePresenter.MOVIE_KEY)
        val genres: List <Genre> = args.getParcelableArrayList(MoviePresenter.MOVIE_GENRES_KEY)


        movieDetails = inState?.getParcelable(MOVIE_DETAILS_KEY)
        trailers = inState?.getParcelableArrayList(MOVIE_TRAILERS_KEY)
        cast = inState?.getParcelableArrayList(MOVIE_CAST_KEY)
        crew = inState?.getParcelableArrayList(MOVIE_CREW_KEY)
        similarMovies = inState?.getParcelableArrayList(MOVIE_SIMILAR_KEY)

        vu.moviePresentedPublisher?.onNext(movie)

        if (movieDetails == null || trailers == null || cast == null || crew == null || similarMovies == null) {
            getMovieVideos(movie, screenName, genres)
        } else {
            val movieInfo = MovieInfo (movie, trailers, movieDetails, genres, screenName, cast, crew, similarMovies)
            vu.updateVu(movieInfo)
        }


        rxSubs.add(vu.onProfileClickedPictureObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe ({movieId ->
                    restService.movieApi.getMovieImages(movieId, DiscoverService.API_KEY).enqueue( object : Callback<ImageResponse>{
                        override fun onResponse(call: Call<ImageResponse>?, response: Response<ImageResponse>?) {
                            val poster = response?.body()?.posters
                            handler.post {
                                vu.goToGallery(poster)
                            }
                        }

                        override fun onFailure(call: Call<ImageResponse>?, t: Throwable?) {
                           Log.d(TAG, "error: $t")
                        }
                    })
                })
        )
    }

    private fun getMovieVideos( movie: Movie, screenName: String?, genres: List<Genre>?) {
        vu?.showLoading()
        restService.movieApi.getMovieVideos(movie.id,  DiscoverService.API_KEY).enqueue( object : Callback<TrailerResponse> {
            override fun onResponse(call: Call<TrailerResponse>?, response: Response<TrailerResponse>?) {
                trailers = response?.body()?.results
                getMovieDetails(movie, screenName, genres, trailers)

            }
            override fun onFailure(call: Call<TrailerResponse>?, t: Throwable?) {
                Log.d(TAG, "error: $t")
            }
        })
    }

    private fun getMovieDetails(movie: Movie, screenName: String?, genres: List<Genre>?, trailers: List<Trailer>?) {
        restService.movieApi.getMovieDetails(movie.id, DiscoverService.API_KEY).enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>?, response: Response<MovieDetails>?) {
                movieDetails = response?.body()
                getMovieCredits(movie, screenName,  movieDetails, genres, trailers)
            }

            override fun onFailure(call: Call<MovieDetails>?, t: Throwable?) {
                Log.d(TAG, "error: $t")
            }
        })
    }

    private fun getMovieCredits (movie: Movie, screenName: String?, movieDetails: MovieDetails?, genres: List<Genre>? ,trailers: List<Trailer>?) {
        restService.movieApi.getCredits( movie.id, DiscoverService.API_KEY).enqueue(object : Callback<MovieCreditsResponse> {
            override fun onResponse(call: Call<MovieCreditsResponse>?, response: Response<MovieCreditsResponse>?) {
                cast = response?.body()?.cast
                crew = response?.body()?.crew
                getSimilarMovies(movie, screenName, genres, movieDetails, trailers, cast, crew)
            }
            override fun onFailure(call: Call<MovieCreditsResponse>?, t: Throwable?) {
                Log.d(TAG, "error: $t")
            }
        })
    }

    private fun getSimilarMovies(movie: Movie, screenName: String?, genres: List<Genre>?, movieDetails: MovieDetails?, trailers: List<Trailer>?, cast: List<Cast>?, crew: List<Crew>?) {
        restService.movieApi.getSimilarMovie( movie.id, DiscoverService.API_KEY).enqueue(object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                similarMovies = response?.body()?.results
                handler.post {
                    vu?.hideLoading()
                    val movieInfo = MovieInfo (movie, trailers, movieDetails, genres, screenName, cast, crew, similarMovies)
                    vu?.updateVu(movieInfo)
                }
            }
            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                Log.d(TAG, "error: $t")
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