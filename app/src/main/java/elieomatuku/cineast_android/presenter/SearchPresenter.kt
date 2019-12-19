package elieomatuku.cineast_android.presenter


import android.os.Bundle
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.model.data.CineastError
import elieomatuku.cineast_android.business.ContentManager
import elieomatuku.cineast_android.business.api.response.MovieResponse
import elieomatuku.cineast_android.business.api.response.PeopleResponse
import elieomatuku.cineast_android.vu.SearchVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber

class SearchPresenter: BasePresenter<SearchVu>() {

    private val contentManager: ContentManager by App.kodein.instance()

    override fun onLink(vu: SearchVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        rxSubs.add(vu.searchQueryObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {argQuery ->
                    vu.showLoading()

                    if (vu.isMovieSearchScreen) {
                        searchMovies(argQuery)
                    } else {
                        searchPeople(argQuery)
                    }
                })
    }


    fun searchMovies(argQuery: String) {
        contentManager.searchMovies(argQuery, object: AsyncResponse<MovieResponse>{
            override fun onSuccess(response: MovieResponse?) {
                handler.post {
                    vu?.hideLoading()
                    vu?.openItemListActivity(response?.results)
                }
            }

            override fun onFail(error: CineastError) {
                Timber.d( "response: ${error}")
                handler.post {
                    vu?.hideLoading()
                }
            }
        })
    }

    fun searchPeople(argQuery: String) {
        contentManager.searchPeople(argQuery, object: AsyncResponse<PeopleResponse> {
            override fun onSuccess(response: PeopleResponse?) {
                handler.post {
                    vu?.hideLoading()
                    vu?.openItemListActivity(response?.results)
                }
            }

            override fun onFail(error: CineastError) {
                Timber.d("response: ${error}")
                handler.post {
                    vu?.hideLoading()
                }
            }
        })
    }
}