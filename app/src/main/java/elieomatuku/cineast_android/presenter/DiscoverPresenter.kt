package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App.Companion.kodein
import elieomatuku.cineast_android.DiscoverContainer
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.model.data.*
import elieomatuku.cineast_android.business.api.response.*
import elieomatuku.cineast_android.business.service.ContentManager
import elieomatuku.cineast_android.business.client.TmdbUserClient
import elieomatuku.cineast_android.vu.DiscoverVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.ArrayList


class DiscoverPresenter : BasePresenter<DiscoverVu>() {
    companion object {
        const val SCREEN_NAME = "Discover"


        const val POPULAR_PEOPLE_KEY = "popular_people"
        const val CONTAINER_KEY = "container_key"

        const val SCREEN_NAME_KEY = "screen_name"

        const val MOVIE_GENRES_KEY = "genres"
        const val MOVIE_KEY = "movieApi"

        const val PEOPLE_KEY = "peopleApi"
    }

    private val contentManager: ContentManager by kodein.instance()
    private val tmdbUserClient: TmdbUserClient by kodein.instance()

    private var discoverContainer: DiscoverContainer? = null
    private var genres: List<Genre>? = listOf()


    override fun onLink(vu: DiscoverVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        fetchSavedState(inState)

        if (!hasSavedState()) {
            vu.showLoading()
            fetchDiscover()
        } else {
            discoverContainer?.let {
                vu.updateView(it, tmdbUserClient.isLoggedIn())
            }
        }

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

    private val asyncResponse: AsyncResponse<MovieResponse> by lazy {
        object : AsyncResponse<MovieResponse> {
            override fun onSuccess(response: MovieResponse?) {
                discoverContainer?.popularMovies = response?.results as List<Movie>
                handler.post {
                    vu?.showLoading()
                }
                contentManager.getNowPlayingMovies(nowPlayingMovieAsyncResponse)
            }

            override fun onFail(error: CineastError) {
                Timber.d("Network Error:$error")
                vu?.hideLoading()
                vu?.updateErrorView(error.status_message)
            }
        }
    }

    val nowPlayingMovieAsyncResponse: AsyncResponse<MovieResponse> by lazy {
        object : AsyncResponse<MovieResponse> {
            override fun onSuccess(response: MovieResponse?) {
                discoverContainer?.nowPlayingMovies = response?.results
                contentManager.getUpcomingMovies(upComingMovieAsyncResponse)
            }

            override fun onFail(error: CineastError) {
                Timber.d("Network Error:$error")
            }
        }
    }

    val upComingMovieAsyncResponse: AsyncResponse<MovieResponse> by lazy {
        object : AsyncResponse<MovieResponse> {
            override fun onSuccess(response: MovieResponse?) {
                discoverContainer?.upcomingMovies = response?.results
                contentManager.getTopRatedMovies(topRatedMovieAsyncResponse)
            }

            override fun onFail(error: CineastError) {
                Timber.d("Network Error:$error")
            }
        }
    }

    val topRatedMovieAsyncResponse: AsyncResponse<MovieResponse> by lazy {
        object : AsyncResponse<MovieResponse> {
            override fun onSuccess(response: MovieResponse?) {
                discoverContainer?.topRatedMovies = response?.results
                contentManager.getPopularPeople(popularPeopleAsyncResponse)
            }

            override fun onFail(error: CineastError) {
                Timber.d("Network Error:$error")
            }
        }
    }

    val popularPeopleAsyncResponse: AsyncResponse<PeopleResponse> by lazy {
        object : AsyncResponse<PeopleResponse> {
            override fun onSuccess(response: PeopleResponse?) {
                discoverContainer?.popularPeople = response?.results
                handler.post {
                    vu?.hideLoading()

                    discoverContainer?.let {
                        vu?.updateView(it, tmdbUserClient.isLoggedIn())
                    }
                }
            }

            override fun onFail(error: CineastError) {
                Timber.e("Network Error:$error")

                handler.post {
                    vu?.updateErrorView(error.status_message)
                }
            }
        }
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

        discoverContainer?.let {
            outState.putParcelable(CONTAINER_KEY, it)
        }

        genres?.let {
            if (it.isNotEmpty()) {
                outState.putParcelableArrayList(MOVIE_GENRES_KEY, it as ArrayList<out Parcelable>)
            }
        }
    }

    private fun hasSavedState(): Boolean {
        return !(discoverContainer == null || genres == null)
    }

    private fun fetchSavedState(inState: Bundle?) {
        discoverContainer = inState?.getParcelable(CONTAINER_KEY)
        genres = inState?.getParcelableArrayList(MOVIE_GENRES_KEY)
    }


    private fun fetchDiscover() {
        discoverContainer = DiscoverContainer()
        contentManager.getPopularMovies(asyncResponse)
        contentManager.getGenres(genreAsyncResponse)
    }
}