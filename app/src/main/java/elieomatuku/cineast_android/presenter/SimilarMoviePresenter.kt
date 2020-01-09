package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import elieomatuku.cineast_android.fragment.SimilarMovieFragment
import elieomatuku.cineast_android.core.model.*
import elieomatuku.cineast_android.fragment.MovieTeamFragment
import elieomatuku.cineast_android.vu.SimilarMovieVu
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.ArrayList


class SimilarMoviePresenter : BasePresenter<SimilarMovieVu>() {
    companion object {
        const val SCREEN_NAME_KEY = "screen_name"
        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"
    }

    private var genres: List<Genre> = listOf()

    override fun onLink(vu: SimilarMovieVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val movieSummary: MovieSummary = args.get(SimilarMovieFragment.MOVIE_SUMMARY) as MovieSummary

        val similarMovies: List<Movie> = movieSummary.similarMovies ?: listOf()
        val movieTitle = movieSummary.movie?.title
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
                    Timber.e("movieSelectObservable failed:$t")
                }
                ))
    }

}