package elieomatuku.cineast_android.presenter


import android.os.Bundle
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.rest.RestApi
import elieomatuku.cineast_android.business.service.DiscoverService
import elieomatuku.cineast_android.business.model.response.MovieResponse
import elieomatuku.cineast_android.business.model.response.PeopleResponse
import elieomatuku.cineast_android.vu.SearchVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class SearchPresenter: BasePresenter<SearchVu>() {

    private val restApi: RestApi by App.kodein.instance()

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
        restApi.movie.getMoviesWithSearch(DiscoverService.API_KEY, argQuery).enqueue( object: Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                handler.post {
                    vu?.hideLoading()
                    vu?.openItemListActivity(response?.body()?.results)
                }
            }
            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                Timber.d( "response: ${t}")
            }
        })
    }

    fun searchPeople(argQuery: String) {
        restApi.people.getPeopleWithSearch(DiscoverService.API_KEY, argQuery).enqueue(object : Callback<PeopleResponse> {
            override fun onResponse(call: Call<PeopleResponse>?, response: Response<PeopleResponse>?) {
                handler.post {
                    vu?.hideLoading()
                    vu?.openItemListActivity(response?.body()?.results)
                }
            }
            override fun onFailure(call: Call<PeopleResponse>?, t: Throwable?) {
                Timber.d("response: ${t}")
            }
        })
    }
}