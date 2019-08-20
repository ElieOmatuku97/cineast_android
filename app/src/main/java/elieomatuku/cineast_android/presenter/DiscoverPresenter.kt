package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.model.data.*
import elieomatuku.cineast_android.business.model.response.*
import elieomatuku.cineast_android.business.service.DiscoverService
import elieomatuku.cineast_android.business.service.UserService
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
        const val MOVIE_KEY = "movie"

        const val PEOPLE_KEY = "people"
    }

    private val discoverClient: DiscoverService by App.kodein.instance()
    private val userService: UserService by App.kodein.instance()

    private var discoverContainer: DiscoverContainer? = null
    private var genres: List<Genre>? = listOf()

    override fun onLink(vu: DiscoverVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        fetchSavedState(inState)

        if (!hasSavedState()) {
            vu.showLoading()
            discoverContainer = DiscoverContainer()
            discoverClient.getPopularMovies(asyncResponse)
            discoverClient.getGenres(genreAsyncResponse)

        } else {
            discoverContainer?.let {
                vu.setWigdet(it, userService.isLoggedIn())
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

                        if (!userService.isLoggedIn()) {
                            userService.getAccessToken(object : AsyncResponse<AccessToken> {
                                override fun onSuccess(result: AccessToken?) {
                                    vu.gotoWebview(result)
                                }

                                override fun onFail(error: String) {
                                    Timber.d("error : $error")
                                }
                            })

                        } else {
                            userService.logout()
                            vu.updateLoginState(false)
                        }
                    }
                })

        rxSubs.add(vu.sessionObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { sessionId ->
                    vu.updateLoginState(!sessionId.isNullOrEmpty())
                })
    }

    private val asyncResponse: AsyncResponse<MovieResponse> by lazy {
        object : AsyncResponse<MovieResponse> {
            override fun onSuccess(result: MovieResponse?) {
                discoverContainer?.popularMovies = result?.results as List<Movie>
                handler.post {
                    vu?.showLoading()
                }
                discoverClient.getNowPlayingMovies(nowPlayingMovieAsyncResponse)
            }

            override fun onFail(error: String) {
                Timber.d("Network Error:$error")
            }
        }
    }

    val nowPlayingMovieAsyncResponse: AsyncResponse<MovieResponse> by lazy {
        object : AsyncResponse<MovieResponse> {
            override fun onSuccess(result: MovieResponse?) {
                discoverContainer?.nowPlayingMovies = result?.results
                discoverClient.getUpcomingMovies(upComingMovieAsyncResponse)
            }

            override fun onFail(error: String) {
                Timber.d("Network Error:$error")

            }
        }
    }

    val upComingMovieAsyncResponse: AsyncResponse<MovieResponse> by lazy {
        object : AsyncResponse<MovieResponse> {
            override fun onSuccess(result: MovieResponse?) {
                discoverContainer?.upcomingMovies = result?.results
                discoverClient.getTopRatedMovies(topRatedMovieAsyncResponse)
            }

            override fun onFail(error: String) {
                Timber.d("Network Error:$error")
            }
        }
    }

    val topRatedMovieAsyncResponse: AsyncResponse<MovieResponse> by lazy {
        object : AsyncResponse<MovieResponse> {
            override fun onSuccess(result: MovieResponse?) {
                discoverContainer?.topRatedMovies = result?.results
                discoverClient.getPopularPeople(popularPeopleAsyncResponse)
            }

            override fun onFail(error: String) {
                Timber.d("Network Error:$error")
            }
        }
    }

    val popularPeopleAsyncResponse: AsyncResponse<PeopleResponse> by lazy {
        object : AsyncResponse<PeopleResponse> {
            override fun onSuccess(result: PeopleResponse?) {
                discoverContainer?.popularPeople = result?.results
                handler.post {
                    vu?.hideLoading()

                    discoverContainer?.let {
                        vu?.setWigdet(it, userService.isLoggedIn())
                    }
                }
            }

            override fun onFail(error: String) {
                Timber.d("Network Error:$error")
            }
        }
    }

    private val genreAsyncResponse: AsyncResponse<GenreResponse> by lazy {
        object : AsyncResponse<GenreResponse> {
            override fun onSuccess(result: GenreResponse?) {
                genres = result?.genres
            }

            override fun onFail(error: String) {
                Timber.d("Network Error:$error")
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
}