package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.activity.ItemListActivity
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.service.DiscoverService
import elieomatuku.cineast_android.business.model.data.*
import elieomatuku.cineast_android.business.model.response.GenreResponse
import elieomatuku.cineast_android.business.service.UserService
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.vu.ItemListVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.*

class ItemListPresenter: BasePresenter <ItemListVu>() {
    companion object {
        const val WIDGET_KEY = "widget"
        const val SCREEN_NAME_KEY = "screen_name"
        const val SCREEN_NAME = "Search"
        const val MOVIE_KEY = "movie"
        const val MOVIE_GENRES_KEY = "genres"
        const val PEOPLE_KEY = "people"
    }

    private val discoverService: DiscoverService by App.kodein.instance()
    private val userService: UserService by App.kodein.instance()
    private var genres: List<Genre>? = listOf()
    private var isWatchList: Boolean = false
    private var isFavoriteList: Boolean = false

    override fun onLink(vu: ItemListVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)
        val listOfWidgets: List<Widget>  = args.getParcelableArrayList(WIDGET_KEY)
        val screenNameRes = args.getInt(SCREEN_NAME_KEY)
        val isUserList = args.getBoolean(UiUtils.USER_LIST_KEY)


        isFavoriteList = args.getBoolean(ItemListActivity.DISPLAY_FAVORITE_LIST) ?: false
        isWatchList = args.getBoolean(ItemListActivity.DISPLAY_WATCH_LIST) ?: false

        vu.updateVu(listOfWidgets, screenNameRes, isUserList)

        vu.userListCheckPublisher?.onNext(isUserList)
        discoverService.getGenres(genreAsyncResponse)
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

        rxSubs.add(vu.personSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({person: Person ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, DiscoverPresenter.SCREEN_NAME)
                    params.putParcelable(PEOPLE_KEY, person)
                    vu.gotoPeople(params)
                })
        )

        rxSubs.add(vu.onMovieRemovedObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    if (isWatchList) {
                        userService.removeMovieFromWatchList(it)
                    } else if (isFavoriteList) {
                        userService.removeMovieFromFavoriteList(it)
                    }
                }, {t: Throwable ->
                    Timber.e("onMovieRemovedObservable failed $t")

                }))
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