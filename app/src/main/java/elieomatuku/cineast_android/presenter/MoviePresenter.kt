package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.model.data.*
import elieomatuku.cineast_android.business.api.response.ImageResponse
import elieomatuku.cineast_android.business.api.response.MovieCreditsResponse
import elieomatuku.cineast_android.business.api.response.MovieResponse
import elieomatuku.cineast_android.business.api.response.TrailerResponse
import elieomatuku.cineast_android.business.client.TmdbUserClient
import elieomatuku.cineast_android.vu.MovieVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.*


class MoviePresenter : BasePresenter<MovieVu>() {
    companion object {
        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"
        const val SCREEN_NAME_KEY = "screen_name"
        const val MOVIE_DETAILS_KEY = "movie_details"
        const val MOVIE_TRAILERS_KEY = "movie_trailers"
        const val MOVIE_CAST_KEY = "movie_cast_key"
        const val MOVIE_CREW_KEY = "movie_crew_key"
        const val MOVIE_SIMILAR_KEY = "movie_similar_key"
    }

    private val tmdbUserClient: TmdbUserClient by App.kodein.instance()
    private val tmdbContentClient: TmdbContentClient by App.kodein.instance()


    var movieDetails: MovieDetails? = null
    var trailers: List<Trailer>? = listOf()
    var cast: List<Cast>? = listOf()
    var crew: List<Crew>? = listOf()
    var similarMovies: List<Movie>? = listOf()


    override fun onLink(vu: MovieVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val screenName = args.getString(SCREEN_NAME_KEY)
        val movie: Movie = args.getParcelable(MOVIE_KEY)
        val genres: List<Genre> = args.getParcelableArrayList(MOVIE_GENRES_KEY)


        Timber.d("movieApi presenter")

        movieDetails = inState?.getParcelable(MOVIE_DETAILS_KEY)
        trailers = inState?.getParcelableArrayList(MOVIE_TRAILERS_KEY)
        cast = inState?.getParcelableArrayList(MOVIE_CAST_KEY)
        crew = inState?.getParcelableArrayList(MOVIE_CREW_KEY)
        similarMovies = inState?.getParcelableArrayList(MOVIE_SIMILAR_KEY)

        vu.moviePresentedPublisher?.onNext(movie)

        if (movieDetails == null || trailers == null || cast == null || crew == null || similarMovies == null) {
            getMovieVideos(movie, screenName, genres)
        } else {
            val movieSummary = MovieSummary(movie, trailers, movieDetails, genres, screenName, cast, crew, similarMovies)
            vu.showMovie(movieSummary)
        }


        rxSubs.add(vu.onProfileClickedPictureObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ movieId ->

                    contentManager.getMovieImages(movieId, object : AsyncResponse<ImageResponse> {
                        override fun onSuccess(response: ImageResponse?) {
                            val poster = response?.posters
                            handler.post {
                                vu.goToGallery(poster)
                            }
                        }

                        override fun onFail(error: CineastError) {
                            Timber.e("error: $error")
                            handler.post {
                                vu.updateErrorView(error.status_message)
                            }
                        }
                    })
                })
        )
    }

    private fun getMovieVideos(movie: Movie, screenName: String?, genres: List<Genre>?) {
        vu?.showLoading()

        contentManager.getMovieVideos(movie, object : AsyncResponse<TrailerResponse> {
            override fun onSuccess(response: TrailerResponse?) {
                trailers = response?.results
                getMovieDetails(movie, screenName, genres, trailers)
            }

            override fun onFail(error: CineastError) {
                Timber.e("error: $error")

                handler.post {
                    vu?.hideLoading()
                    vu?.updateErrorView(error.status_message)
                }
            }
        })
    }

    private fun getMovieDetails(movie: Movie, screenName: String?, genres: List<Genre>?, trailers: List<Trailer>?) {
        contentManager.getMovieDetails(movie, object : AsyncResponse<MovieDetails> {
            override fun onSuccess(response: MovieDetails?) {
                movieDetails = response
                getMovieCredits(movie, screenName, movieDetails, genres, trailers)
            }

            override fun onFail(error: CineastError) {
                Timber.e("error: $error")
                vu?.updateErrorView(error.status_message)
            }
        })
    }

    private fun getMovieCredits(movie: Movie, screenName: String?, movieDetails: MovieDetails?, genres: List<Genre>?, trailers: List<Trailer>?) {
        contentManager.getMovieCredits(movie, object : AsyncResponse<MovieCreditsResponse> {
            override fun onSuccess(response: MovieCreditsResponse?) {
                cast = response?.cast
                crew = response?.crew
                getSimilarMovies(movie, screenName, genres, movieDetails, trailers, cast, crew)
            }

            override fun onFail(error: CineastError) {
                Timber.e("error: $error")
                vu?.updateErrorView(error.status_message)
            }
        })
    }

    private fun getSimilarMovies(movie: Movie, screenName: String?, genres: List<Genre>?, movieDetails: MovieDetails?, trailers: List<Trailer>?, cast: List<Cast>?, crew: List<Crew>?) {
        contentManager.getSimilarMovie(movie, object : AsyncResponse<MovieResponse> {
            override fun onSuccess(response: MovieResponse?) {
                similarMovies = response?.results
                val movieSummary = MovieSummary(movie, trailers, movieDetails, genres, screenName, cast, crew, similarMovies)

                if (!tmdbUserClient.isLoggedIn()) {
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

            override fun onFail(error: CineastError) {
                Timber.e("error: $error")
                vu?.updateErrorView(error.status_message)
            }
        })
    }


    private fun checkIfMovieInWatchList(movieSummary: MovieSummary) {
        tmdbContentClient.getWatchList(object : AsyncResponse<List<Movie>> {
            override fun onSuccess(result: List<Movie>?) {
                Timber.d("watch list result: ${result} \n movieApi selected: ${movieSummary.movie}")
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
        tmdbContentClient.getFavoriteList(object : AsyncResponse<List<Movie>> {
            override fun onSuccess(result: List<Movie>?) {
                Timber.d("favorite list result: ${result} \n movieApi selected: ${movieSummary.movie}")
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

        cast?.let {
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