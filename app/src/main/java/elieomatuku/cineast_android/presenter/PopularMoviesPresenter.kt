package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.service.DiscoverService
import elieomatuku.cineast_android.business.model.data.Genre
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.model.response.GenreResponse
import elieomatuku.cineast_android.business.model.response.MovieResponse
import elieomatuku.cineast_android.vu.PopularMoviesVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber


class PopularMoviesPresenter : BasePresenter <PopularMoviesVu>() {
    companion object {
        const val SCREEN_NAME_KEY = "screen_name"
        const val SCREEN_NAME = "Search"

        const val MOVIE_KEY = "movie"
        const val MOVIE_GENRES_KEY = "genres"
    }

    private val discoverClient: DiscoverService by App.kodein.instance()
    private var genres: List<Genre>? = listOf()

    override fun onLink(vu: PopularMoviesVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        vu.showLoading()
        discoverClient.getPopularMovies(asyncResponse)
        discoverClient.getGenres(genreAsyncResponse)


        rxSubs.add(vu.movieSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ movie: Movie ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, SCREEN_NAME)
                    params.putParcelable(MOVIE_KEY, movie)
                    params.putParcelableArrayList(MOVIE_GENRES_KEY, genres as ArrayList<out Parcelable>)
                    vu.gotoMovie(params)

                }, {t: Throwable ->
                    Timber.d( "movieSelectObservable failed:$t")
                }))
    }

    private val asyncResponse: AsyncResponse<MovieResponse> by lazy{
        object: AsyncResponse<MovieResponse> {
            override fun onSuccess(result: MovieResponse?) {
                val popularMovie = result?.results

                handler.post {
                    vu?.hideLoading()

                    if (popularMovie != null) {
                        vu?.populateGridView(popularMovie)
                    } else {
                        Timber.d("Error Network Result : $popularMovie")
                    }
                }
            }

            override fun onFail(error: String) {
                Timber.d( "Network Error:$error")
            }
        }
    }

    private val genreAsyncResponse: AsyncResponse<GenreResponse> by lazy {
        object: AsyncResponse<GenreResponse> {
            override fun onSuccess(result: GenreResponse?) {
                genres = result?.genres
            }
            override fun onFail(error: String) {
                Timber.d("Network Error:$error")
            }
        }
    }
}