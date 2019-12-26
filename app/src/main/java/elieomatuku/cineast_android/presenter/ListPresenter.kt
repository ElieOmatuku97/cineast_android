package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.model.data.*
import elieomatuku.cineast_android.business.api.response.GenreResponse
import elieomatuku.cineast_android.business.client.TmdbUserClient
import elieomatuku.cineast_android.vu.ListVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.*

abstract class ListPresenter<V>: BasePresenter <V>() where V: ListVu   {
    companion object {
        const val WIDGET_KEY = "content"
        const val SCREEN_NAME_KEY = "screen_name"
        const val SCREEN_NAME = "Search"
        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"
        const val PEOPLE_KEY = "peopleApi"
    }

    protected val tmdbUserClient: TmdbUserClient by App.kodein.instance()
    protected val tmdbContentClient: TmdbContentClient by App.kodein.instance()
    private var genres: List<Genre>? = listOf()

    override fun onLink(vu: V, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        contentManager.getGenres(genreAsyncResponse)
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
}