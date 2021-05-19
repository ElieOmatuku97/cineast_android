package elieomatuku.cineast_android.ui.search

import android.os.Bundle
import elieomatuku.cineast_android.business.api.response.MovieResponse
import elieomatuku.cineast_android.business.api.response.PersonalityResponse
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.core.model.CineastError
import elieomatuku.cineast_android.ui.common_presenter.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

class SearchPresenter : BasePresenter<SearchVu>() {

    override fun onLink(vu: SearchVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        rxSubs.add(
            vu.searchQueryObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { argQuery ->
                    vu.showLoading()
                    Timber.d("search screen isMovieSearchScreen: ${vu.isMovieSearchScreen}")

                    if (vu.isMovieSearchScreen) {
                        searchMovies(argQuery)
                    } else {
                        searchPeople(argQuery)
                    }
                }
        )
    }

    private fun searchMovies(argQuery: String) {
        contentService.searchMovies(
            argQuery,
            object : AsyncResponse<MovieResponse> {
                override fun onSuccess(response: MovieResponse?) {
                    handler.post {
                        vu?.hideLoading()
                        vu?.showSearchResults(response?.results)
                    }
                }

                override fun onFail(error: CineastError) {
                    Timber.d("response: $error")
                    handler.post {
                        vu?.hideLoading()
                    }
                }
            }
        )
    }

    private fun searchPeople(argQuery: String) {
        contentService.searchPeople(
            argQuery,
            object : AsyncResponse<PersonalityResponse> {
                override fun onSuccess(response: PersonalityResponse?) {
                    handler.post {
                        vu?.hideLoading()
                        vu?.showSearchResults(response?.results)
                    }
                }

                override fun onFail(error: CineastError) {
                    Timber.d("response: $error")
                    handler.post {
                        vu?.hideLoading()
                    }
                }
            }
        )
    }
}
