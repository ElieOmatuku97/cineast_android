package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.model.data.CineastError
import elieomatuku.cineast_android.business.ContentManager
import elieomatuku.cineast_android.model.data.Genre
import elieomatuku.cineast_android.model.data.Movie
import elieomatuku.cineast_android.business.api.response.GenreResponse
import elieomatuku.cineast_android.vu.PopularMoviesVu
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance
import timber.log.Timber



class PopularMoviesPresenter : BasePresenter <PopularMoviesVu>() {
    companion object {
        const val SCREEN_NAME_KEY = "screen_name"
        const val SCREEN_NAME = "Search"

        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"
    }

    private val contentManager: ContentManager by App.kodein.instance()
    private var genres: List<Genre>? = listOf()




    override fun onLink(vu: PopularMoviesVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        vu.showLoading()
        fetchMovies()

        rxSubs.add(contentManager.popularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    vu.hideLoading()
                    vu.populateGridView(it)
                }, { error ->
                    Timber.e("Unable to get discover container $error")
                    vu.updateErrorView(error.message)
                })
        )

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


        rxSubs.add(connectionService.connectionChangedObserver
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ hasConnection ->

                    if (hasConnection) {
                        vu.showLoading()
                        fetchMovies()
                    }
                    Timber.d("connectionChangedObserver: hasConnection = $hasConnection, hasEmptyState = ")

                }, { t: Throwable ->

                    Timber.e(t, "Connection Change Observer failed")

                }))
    }


    private val genreAsyncResponse: AsyncResponse<GenreResponse> by lazy {
        object: AsyncResponse<GenreResponse> {
            override fun onSuccess(response: GenreResponse?) {
                genres = response?.genres
            }
            override fun onFail(error: CineastError) {
                Timber.d("Network Error:$error")
            }
        }
    }


    private fun fetchMovies() {
        contentManager.getGenres(genreAsyncResponse)
    }
}