package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.model.data.CineastError
import elieomatuku.cineast_android.model.data.Genre
import elieomatuku.cineast_android.model.data.Movie
import elieomatuku.cineast_android.model.data.KnownFor
import elieomatuku.cineast_android.business.api.response.GenreResponse
import elieomatuku.cineast_android.vu.KnownForVu
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.ArrayList

class KnownForPresenter: BasePresenter <KnownForVu>() {
    companion object {
        val LOG_TAG = KnownForPresenter::class.java.simpleName
        const val PEOPLE_CAST_KEY = "people_cast"
        const val MOVIE_KEY = "movieApi"
        const val PEOPLE_NAME_KEY = "people_name"
        const val SCREEN_NAME_KEY = "screen_name"

    }


    private var genres: List<Genre>? = listOf()

    override fun onLink(vu: KnownForVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val knownFor: List<KnownFor> = args.getParcelableArrayList(PEOPLE_CAST_KEY)
        val peopleName: String = args.getString(PEOPLE_NAME_KEY)
        vu.updateVu(knownFor)
        contentManager.getGenres(genreAsyncResponse)

        rxSubs.add(vu.itemSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ movieId: Int ->

                    Log.d(LOG_TAG, "movieApi cast_id: $movieId")
                    getMovie(movieId, peopleName)

                }, {t: Throwable ->
                    Log.d(LOG_TAG, "movieSelectObservable failed:$t")
                }
                ))
    }


    private fun getMovie(movieId: Int, peopleName: String? = null) {
        contentManager.getMovie(movieId, object: AsyncResponse<Movie> {
            override fun onSuccess(response: Movie?) {
                val movie : Movie = response as Movie
                handler.post {
                    val params = Bundle()
                    if (peopleName != null ) {
                        params.putString(SCREEN_NAME_KEY, peopleName)
                    }
                    params.putParcelable(MOVIE_KEY, movie)
                    params.putParcelableArrayList(SimilarMoviePresenter.MOVIE_GENRES_KEY, genres as ArrayList<out Parcelable>)
                    vu?.gotoMovie(params)
                }
            }

            override fun onFail(error: CineastError) {
                Timber.e("error: $error")
            }
        })
    }

    private val genreAsyncResponse: AsyncResponse<GenreResponse> by lazy {
        object : AsyncResponse<GenreResponse> {
            override fun onSuccess(response: GenreResponse?) {
                genres = response?.genres
            }

            override fun onFail(error: CineastError) {
                Log.d(LOG_TAG, "Network Error:$error")
            }
        }
    }
}