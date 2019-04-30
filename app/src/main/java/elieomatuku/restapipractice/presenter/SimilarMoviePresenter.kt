package elieomatuku.restapipractice.presenter

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import elieomatuku.restapipractice.App
import elieomatuku.restapipractice.fragment.SimilarMovieFragment
import elieomatuku.restapipractice.business.business.callback.AsyncResponse
import elieomatuku.restapipractice.business.business.model.response.GenreResponse
import elieomatuku.restapipractice.business.business.service.DiscoverService
import elieomatuku.restapipractice.business.business.model.data.*
import elieomatuku.restapipractice.vu.SimilarMovieVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import java.util.ArrayList


class SimilarMoviePresenter: BasePresenter<SimilarMovieVu> (){
    companion object {
        val LOG_TAG = SimilarMoviePresenter::class.java.simpleName
        const val MOVIE_SIMILAR_MOVIES_KEY = "movie_similar_movies"
        const val SCREEN_NAME_KEY = "screen_name"
        const val MOVIE_KEY = "movie"
        const val MOVIE_GENRES_KEY = "genres"
    }
    private val discoverClient: DiscoverService by App.kodein.instance()
    private var genres: List<Genre>? = listOf()

    override fun onLink(vu: SimilarMovieVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)
        val similarMovies: List<Movie> = args.getParcelableArrayList(MOVIE_SIMILAR_MOVIES_KEY)
        val movieTitle = args.getString(SimilarMovieFragment.MOVIE_TITLE)
        vu.updateVu(similarMovies)

        discoverClient.getGenres(genreAsyncResponse)

        rxSubs.add(vu.itemSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ movie: Movie ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, movieTitle)
                    params.putParcelable(MOVIE_KEY, movie)
                    params.putParcelableArrayList(MOVIE_GENRES_KEY, genres as ArrayList<out Parcelable>)
                    vu.gotoMovie(params)
                }, {t: Throwable ->
                    Log.d(LOG_TAG, "movieSelectObservable failed:$t")
                }
                ))
    }

    private val genreAsyncResponse: AsyncResponse<GenreResponse> by lazy {
        object: AsyncResponse<GenreResponse> {
            override fun onSuccess(result: GenreResponse?) {
                genres = result?.genres
            }

            override fun onFail(error: String) {
                Log.d(LOG_TAG, "Network Error:$error")
            }
        }
    }
}