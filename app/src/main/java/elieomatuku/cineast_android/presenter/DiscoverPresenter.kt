package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App.Companion.kodein
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.model.data.*
import elieomatuku.cineast_android.business.api.response.*
import elieomatuku.cineast_android.business.ContentManager
import elieomatuku.cineast_android.business.client.TmdbUserClient
import elieomatuku.cineast_android.vu.DiscoverVu
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.ArrayList


class DiscoverPresenter : BasePresenter<DiscoverVu>() {
    companion object {
        const val SCREEN_NAME = "Discover"
        const val SCREEN_NAME_KEY = "screen_name"
        const val MOVIE_GENRES_KEY = "genres"
        const val MOVIE_KEY = "movieApi"
        const val PEOPLE_KEY = "peopleApi"
    }

    private val contentManager: ContentManager by kodein.instance()
    private val tmdbUserClient: TmdbUserClient by kodein.instance()

    private var genres: List<Genre>? = listOf()


    override fun onLink(vu: DiscoverVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        vu.showLoading()
        fetchDiscover()

        rxSubs.add(contentManager.discoverContent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    vu.hideLoading()
                    Timber.d("discoverContent: ${it.popularMovies}")
                    vu.updateView(it, tmdbUserClient.isLoggedIn())

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



        rxSubs.add(vu.personSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { actor: Person ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, SCREEN_NAME)
                    params.putParcelable(PEOPLE_KEY, actor)
                    vu.gotoPeople(params)
                }
        )

        rxSubs.add(vu.loginClickObservable
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
                })

        rxSubs.add(vu.sessionObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { sessionId ->
                    vu.updateLoginState(!sessionId.isNullOrEmpty())
                })



        rxSubs.add(connectionService.connectionChangedObserver
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ hasConnection ->

                    if (hasConnection) {
                        vu.showLoading()
                        fetchDiscover()
                    } else {
                        vu.hideLoading()
                    }

                    Timber.d("connectionChangedObserver: hasConnection = $hasConnection, hasEmptyState = ")

                }, { t: Throwable ->

                    Timber.e(t, "Connection Change Observer failed")

                }))
    }


    private val genreAsyncResponse: AsyncResponse<GenreResponse> by lazy {
        object : AsyncResponse<GenreResponse> {
            override fun onSuccess(response: GenreResponse?) {
                genres = response?.genres
            }

            override fun onFail(error: CineastError) {
                Timber.i("Network Error:$error")
            }
        }
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
        launch {
            contentManager.fetchDiscoverContent()
        }
        contentManager.getGenres(genreAsyncResponse)
    }

}