package elieomatuku.restapipractice.presenter


import android.os.Bundle
import elieomatuku.restapipractice.App
import elieomatuku.restapipractice.business.business.service.RestService
import elieomatuku.restapipractice.business.business.service.DiscoverService
import elieomatuku.restapipractice.business.business.model.response.MovieResponse
import elieomatuku.restapipractice.business.business.model.response.PeopleResponse
import elieomatuku.restapipractice.vu.SearchVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class SearchPresenter: BasePresenter<SearchVu>() {

    private val restService: RestService by App.kodein.instance()

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
        restService.movieApi.getMoviesWithSearch(DiscoverService.API_KEY, argQuery).enqueue( object: Callback<MovieResponse> {
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
        restService.peopleApi.getPeopleWithSearch(DiscoverService.API_KEY, argQuery).enqueue(object : Callback<PeopleResponse> {
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