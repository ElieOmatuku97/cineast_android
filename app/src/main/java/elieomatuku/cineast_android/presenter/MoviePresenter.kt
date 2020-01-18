package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.core.model.*
import elieomatuku.cineast_android.business.api.response.ImageResponse
import elieomatuku.cineast_android.business.client.TmdbUserClient
import elieomatuku.cineast_android.vu.MovieVu
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.launch
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


    var movieFacts: MovieFacts? = null
    var trailers: List<Trailer>? = listOf()
    var cast: List<Cast>? = listOf()
    var crew: List<Crew>? = listOf()
    var similarMovies: List<Movie>? = listOf()




    override fun onLink(vu: MovieVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val screenName = args.getString(SCREEN_NAME_KEY)
        val movie: Movie = args.getParcelable(MOVIE_KEY)
        val genres: List<Genre> = args.getParcelableArrayList(MOVIE_GENRES_KEY)


        movieFacts = inState?.getParcelable(MOVIE_DETAILS_KEY)
        trailers = inState?.getParcelableArrayList(MOVIE_TRAILERS_KEY)
        cast = inState?.getParcelableArrayList(MOVIE_CAST_KEY)
        crew = inState?.getParcelableArrayList(MOVIE_CREW_KEY)
        similarMovies = inState?.getParcelableArrayList(MOVIE_SIMILAR_KEY)

        vu.moviePresentedPublisher?.onNext(movie)

        vu.countingIdlingResource.increment()

        if (movieFacts == null || trailers == null || cast == null || crew == null || similarMovies == null) {
            getMovieVideos(movie, screenName, genres)
        } else {
            val movieSummary = MovieSummary(movie, trailers, movieFacts, genres, screenName, cast, crew, similarMovies)
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

        launch {
            trailers = contentManager.getMovieVideos(movie)?.results
            getMovieDetails(movie, screenName, genres, trailers)

        }
    }

    private fun getMovieDetails(movie: Movie, screenName: String?, genres: List<Genre>?, trailers: List<Trailer>?) {
        launch {
            movieFacts = contentManager.getMovieDetails(movie)
            getMovieCredits(movie, screenName, movieFacts, genres, trailers)
        }
    }

    private fun getMovieCredits(movie: Movie, screenName: String?, movieFacts: MovieFacts?, genres: List<Genre>?, trailers: List<Trailer>?) {
        launch {
            val response = contentManager.getMovieCredits(movie)
            cast = response?.cast
            crew = response?.crew
            getSimilarMovies(movie, screenName, genres, movieFacts, trailers, cast, crew)
        }
    }

    private fun getSimilarMovies(movie: Movie, screenName: String?, genres: List<Genre>?, movieFacts: MovieFacts?, trailers: List<Trailer>?, cast: List<Cast>?, crew: List<Crew>?) {

        launch {
            val response = contentManager.getSimilarMovie(movie)
            similarMovies = response?.results
            val movieSummary = MovieSummary(movie, trailers, movieFacts, genres, screenName, cast, crew, similarMovies)


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

        movieFacts?.let {
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