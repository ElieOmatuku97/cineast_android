package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import elieomatuku.cineast_android.fragment.SimilarMovieFragment
import elieomatuku.cineast_android.model.data.*
import elieomatuku.cineast_android.vu.SimilarMovieVu
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.ArrayList


class SimilarMoviePresenter : BasePresenter<SimilarMovieVu>() {
    companion object {
        val LOG_TAG = SimilarMoviePresenter::class.java.simpleName
        const val MOVIE_SIMILAR_MOVIES_KEY = "movie_similar_movies"
        const val SCREEN_NAME_KEY = "screen_name"
        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"
    }

    private var genres: List<Genre> = listOf()

    override fun onLink(vu: SimilarMovieVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)
        val similarMovies: List<Movie> = args.getParcelableArrayList(MOVIE_SIMILAR_MOVIES_KEY)
        val movieTitle = args.getString(SimilarMovieFragment.MOVIE_TITLE)
        vu.updateVu(similarMovies)


        rxSubs.add(contentManager.genres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("genres from database: ${it}")
                    genres = it
                }, { error ->
                    Timber.e("Unable to get genres $error")

                })
        )


        rxSubs.add(vu.itemSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ movie: Movie ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, movieTitle)
                    params.putParcelable(MOVIE_KEY, movie)
                    params.putParcelableArrayList(MOVIE_GENRES_KEY, genres as ArrayList<out Parcelable>)
                    vu.gotoMovie(params)
                }, { t: Throwable ->
                    Log.d(LOG_TAG, "movieSelectObservable failed:$t")
                }
                ))
    }

}