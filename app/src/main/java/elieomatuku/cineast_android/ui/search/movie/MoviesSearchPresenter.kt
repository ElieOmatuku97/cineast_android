package elieomatuku.cineast_android.ui.search.movie

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.core.model.Genre
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.ui.common_presenter.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class MoviesSearchPresenter : BasePresenter<MoviesSearchVu>() {
    companion object {
        const val SCREEN_NAME_KEY = "screen_name"
        const val SCREEN_NAME = "Search"

        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"
    }

    private var genres: List<Genre>? = listOf()


    override fun onLink(vu: MoviesSearchVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        vu.showLoading()


        rxSubs.add(contentService.genres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("genres from database: ${it}")
                    genres = it
                }, { error ->
                    Timber.e("Unable to get genres $error")

                })
        )


        rxSubs.add(contentService.popularMovies()
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

                }, { t: Throwable ->
                    Timber.d("movieSelectObservable failed:$t")
                }))


        rxSubs.add(connectionService.connectionChangedObserver
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ hasConnection ->

                    if (hasConnection) {
                        vu.showLoading()
                    }
                    Timber.d("connectionChangedObserver: hasConnection = $hasConnection, hasEmptyState = ")

                }, { t: Throwable ->

                    Timber.e(t, "Connection Change Observer failed")

                }))
    }


}
