package elieomatuku.cineast_android.details.movie

import android.os.Bundle
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.core.model.*
import elieomatuku.cineast_android.business.client.TmdbUserClient
import elieomatuku.cineast_android.presenter.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import timber.log.Timber


class MoviePresenter : BasePresenter<MovieVu>() {
    companion object {
        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"
        const val SCREEN_NAME_KEY = "screen_name"
    }

    private val tmdbUserClient: TmdbUserClient by App.kodein.instance()

    private var movieFacts: MovieFacts? = null
    private var trailers: List<Trailer>? = listOf()
    private var cast: List<Cast>? = listOf()
    private var crew: List<Crew>? = listOf()
    private var similarMovies: List<Movie>? = listOf()


    override fun onLink(vu: MovieVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val screenName = args.getString(SCREEN_NAME_KEY)
        val movie: Movie? = args.getParcelable(MOVIE_KEY)
        val genres: List<Genre>? = args.getParcelableArrayList(MOVIE_GENRES_KEY)


        Timber.d("onLink called")

        vu.countingIdlingResource.increment()

        movie?.let {
            vu.moviePresentedPublisher?.onNext(movie)
            getMovieVideos(movie, screenName, genres)
        }

        rxSubs.add(vu.onProfileClickedPictureObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { movieId ->
                    launch {
                        getMovieImages(movieId)
                    }
                }
        )

        rxSubs.add(vu.segmentedButtonsObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { displayAndMovieSummary ->
                    vu.gotoTab(displayAndMovieSummary)
                }
        )
    }

    private fun getMovieVideos(movie: Movie, screenName: String?, genres: List<Genre>?) {
        vu?.showLoading()
        launch {
            trailers = contentService.getMovieVideos(movie)?.results
            getMovieDetails(movie, screenName, genres, trailers)

        }
    }

    private fun getMovieDetails(movie: Movie, screenName: String?, genres: List<Genre>?, trailers: List<Trailer>?) {
        launch {
            movieFacts = contentService.getMovieDetails(movie)
            getMovieCredits(movie, screenName, movieFacts, genres, trailers)
        }
    }

    private fun getMovieCredits(movie: Movie, screenName: String?, movieFacts: MovieFacts?, genres: List<Genre>?, trailers: List<Trailer>?) {
        launch {
            val response = contentService.getMovieCredits(movie)
            cast = response?.cast
            crew = response?.crew
            getSimilarMovies(movie, screenName, genres, movieFacts, trailers, cast, crew)
        }
    }

    private fun getSimilarMovies(movie: Movie, screenName: String?, genres: List<Genre>?, movieFacts: MovieFacts?, trailers: List<Trailer>?, cast: List<Cast>?, crew: List<Crew>?) {
        launch {
            val response = contentService.getSimilarMovie(movie)
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
        launch {
            val movieResponse = contentService.getWatchList()

            if (movieResponse.isSuccess) {

                val movies = movieResponse.getOrNull()?.results

                val isInWatchList = movies?.let {
                    it.contains(movieSummary.movie)
                } ?: false

                launch(Dispatchers.Main) {
                    vu?.watchListCheckPublisher?.onNext(isInWatchList)
                }

                checkIfMovieInFavoriteList(movieSummary)

            } else {
                launch(Dispatchers.Main) {
                    vu?.hideLoading()
                    vu?.showMovie(movieSummary)
                }
            }
        }
    }


    private fun checkIfMovieInFavoriteList(movieSummary: MovieSummary) {
        launch {
            val movieResponse = contentService.getFavoriteList()
            if (movieResponse.isSuccess) {
                val movies = movieResponse.getOrNull()?.results
                val isInFavoriteList = movies?.let {
                    it.contains(movieSummary.movie)
                } ?: false

                launch(Dispatchers.Main) {
                    vu?.hideLoading()
                    vu?.favoriteListCheckPublisher?.onNext(isInFavoriteList)
                    vu?.showMovie(movieSummary)
                    Timber.d("isInFavoriteList: $isInFavoriteList")
                }

            } else {
                launch(Dispatchers.Main) {
                    vu?.hideLoading()
                    vu?.showMovie(movieSummary)
                }
            }
        }
    }

    private suspend fun getMovieImages(movieId: Int) {
        val imageResponse = contentService.getMovieImages(movieId)
        if (imageResponse.isSuccess) {
            val posters = imageResponse.getOrNull()?.posters

            launch(Dispatchers.Main) {
                vu?.goToGallery(posters)
            }

        } else {
            launch(Dispatchers.Main) {
                vu?.updateErrorView(imageResponse.exceptionOrNull()?.message)
            }
        }
    }
}