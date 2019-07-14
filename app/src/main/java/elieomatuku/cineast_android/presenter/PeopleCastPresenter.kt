package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.service.RestService
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.service.DiscoverService
import elieomatuku.cineast_android.business.model.data.Genre
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.model.data.PeopleCast
import elieomatuku.cineast_android.business.model.response.GenreResponse
import elieomatuku.cineast_android.vu.PeopleCastVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class PeopleCastPresenter: BasePresenter <PeopleCastVu>() {
    companion object {
        val LOG_TAG = PeopleCastPresenter::class.java.simpleName
        const val PEOPLE_CAST_KEY = "people_cast"
        const val MOVIE_KEY = "movie"
        const val PEOPLE_NAME_KEY = "people_name"
        const val SCREEN_NAME_KEY = "screen_name"

    }
    private val restService: RestService by App.kodein.instance()
    private val discoverClient: DiscoverService by App.kodein.instance()
    private var genres: List<Genre>? = listOf()

    override fun onLink(vu: PeopleCastVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val peopleCast: List<PeopleCast> = args.getParcelableArrayList(PEOPLE_CAST_KEY)
        val peopleName: String = args.getString(PEOPLE_NAME_KEY)
        vu.updateVu(peopleCast)
        discoverClient.getGenres(genreAsyncResponse)

        rxSubs.add(vu.itemSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ movieId: Int ->

                    Log.d(LOG_TAG, "movie cast_id: $movieId")
                    getMovie(movieId, peopleName)

                }, {t: Throwable ->
                    Log.d(LOG_TAG, "movieSelectObservable failed:$t")
                }
                ))
    }


    private fun getMovie(movieId: Int, peopleName: String? = null) {
        restService.movieApi.getMovie(movieId, DiscoverService.API_KEY).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>?, response: Response<Movie>?) {
                val movie : Movie = response?.body() as Movie
                Log.d(LOG_TAG, "response: ${response?.body()}")

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

            override fun onFailure(call: Call<Movie>?, t: Throwable?) {
                Log.d(LOG_TAG, "error: $t")
            }
        })
    }

    private val genreAsyncResponse: AsyncResponse<GenreResponse> by lazy {
        object : AsyncResponse<GenreResponse> {
            override fun onSuccess(result: GenreResponse?) {
                genres = result?.genres
            }

            override fun onFail(error: String) {
                Log.d(LOG_TAG, "Network Error:$error")
            }
        }
    }

}