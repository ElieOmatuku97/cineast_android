package elieomatuku.cineast_android.ui.discover

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App.Companion.kodein
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.TmdbUserClient
import elieomatuku.cineast_android.core.model.AccessToken
import elieomatuku.cineast_android.core.model.CineastError
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.core.model.Genre
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.ui.base.BasePresenter
import elieomatuku.cineast_android.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.ArrayList

class DiscoverPresenter : BasePresenter<DiscoverVu>() {
    companion object {
        const val SCREEN_NAME = "Discover"
        const val MOVIE_GENRES_KEY = "genres"
        const val MOVIE_KEY = "movieApi"
        const val PEOPLE_KEY = "peopleApi"
    }

    private val tmdbUserClient: TmdbUserClient by kodein.instance()
    private var genres: List<Genre>? = listOf()

    override fun onLink(vu: DiscoverVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        vu.showLoading()
        fetchDiscover()

        rxSubs.add(
            contentService.discoverContent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        vu.hideLoading()
                        vu.updateView(it, tmdbUserClient.isLoggedIn())
                    },
                    { error ->
                        Timber.e("Unable to get discover container $error")
                        vu.updateErrorView(error.message)
                    }
                )
        )

        rxSubs.add(
            contentService.genres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Timber.d("genres from database: $it")
                        genres = it
                    },
                    { error ->
                        Timber.e("Unable to get genres $error")
                    }
                )
        )

        rxSubs.add(
            vu.movieSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { movie: Movie ->
                        val params = Bundle()
                        params.putString(Constants.SCREEN_NAME_KEY, SCREEN_NAME)
                        params.putParcelable(MOVIE_KEY, movie)
                        params.putParcelableArrayList(MOVIE_GENRES_KEY, genres as ArrayList<out Parcelable>)
                        vu.gotoMovie(params)
                    },
                    { t: Throwable ->
                        Timber.d("movieSelectObservable failed:$t")
                    }
                )
        )

        rxSubs.add(
            vu.personSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { actor: Content ->
                    val params = Bundle()
                    params.putString(Constants.SCREEN_NAME_KEY, SCREEN_NAME)
                    params.putParcelable(PEOPLE_KEY, actor)
                    vu.gotoPeople(params)
                }
        )

        rxSubs.add(
            vu.loginClickObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { clicked ->
                    if (clicked) {

                        if (!tmdbUserClient.isLoggedIn()) {
                            tmdbUserClient.getAccessToken(object : AsyncResponse<AccessToken> {
                                override fun onSuccess(response: AccessToken?) {
                                    vu.gotoWebview(response)
                                }

                                override fun onFail(error: CineastError) {
                                    Timber.d("error : $error")
                                }
                            })
                        } else {
                            tmdbUserClient.logout()
                            vu.updateLoginState(false)
                        }
                    }
                }
        )

        rxSubs.add(
            vu.sessionObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { sessionResponse ->
                    vu.updateLoginState(!sessionResponse.first.isNullOrEmpty())
                }
        )

        rxSubs.add(
            connectionService.connectionChangedObserver
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { hasConnection ->
                        if (hasConnection) {
                            vu.showLoading()
                            fetchDiscover()
                        } else {
                            vu.hideLoading()
                        }

                        Timber.d("connectionChangedObserver: hasConnection = $hasConnection, hasEmptyState = ")
                    },
                    { t: Throwable ->

                        Timber.e(t, "Connection Change Observer failed")
                    }
                )
        )

        rxSubs.add(
            vu.refreshObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Timber.i("Successful Refresh!")
                        contentService.downloadContent()
                    },
                    {
                        Timber.e("Refreshed Failed with $it")
                    }
                )
        )
    }

    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)

        genres?.let {
            if (it.isNotEmpty()) {
                outState.putParcelableArrayList(MOVIE_GENRES_KEY, it as ArrayList<out Parcelable>)
            }
        }
    }

    private fun fetchDiscover() {
        contentService.fetchDiscoverContent()
    }
}
