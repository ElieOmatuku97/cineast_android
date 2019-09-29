package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.service.DiscoverService
import elieomatuku.cineast_android.business.model.data.People
import elieomatuku.cineast_android.business.model.data.Person
import elieomatuku.cineast_android.business.model.response.PeopleResponse
import elieomatuku.cineast_android.vu.PopularPeopleVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.ArrayList


class PopularPeoplePresenter : BasePresenter <PopularPeopleVu>() {
    companion object {
        const val POPULAR_PEOPLE_KEY = "popular_people"
        const val SCREEN_NAME_KEY = "screen_name"
        const val SCREEN_NAME = "Search"
        const val PEOPLE_KEY = "people"
    }


    private val discoverClient: DiscoverService by App.kodein.instance()
    private var popularPeople: List <People>? = listOf()


    override fun onLink(vu: PopularPeopleVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        popularPeople = inState?.getParcelableArrayList(POPULAR_PEOPLE_KEY)

        if (popularPeople != null) {
            vu.updateList(popularPeople)
        } else {
            vu.showLoading()
            fetchPopularPeople()
        }

        rxSubs.add(vu.peopleSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({actor: Person ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, SCREEN_NAME)
                    params.putParcelable(PEOPLE_KEY, actor)
                    vu.gotoPeople(params)
                }))

        rxSubs.add(connectionService.connectionChangedObserver
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ hasConnection ->
                    Timber.d("connectionChangedObserver: hasConnection = $hasConnection, hasEmptyState = ")

                    if (hasConnection) {
                        vu.showLoading()
                        fetchPopularPeople()
                    }

                }, { t: Throwable ->

                    Timber.e(t, "Connection Change Observer failed")

                }))

    }

    val popularPeopleAsyncResponse: AsyncResponse<PeopleResponse> by lazy {
        object : AsyncResponse<PeopleResponse> {
            override fun onSuccess(result: PeopleResponse?) {
                popularPeople = result?.results
                handler.post {
                    vu?.hideLoading()

                    if (popularPeople != null) {
                        vu?.updateList(popularPeople)
                    }
                }
            }

            override fun onFail(error: String) {
                vu?.hideLoading()
                Timber.d( "Network Error:$error")
            }
        }
    }

    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)
        popularPeople?.let{
            outState.putParcelableArrayList(DiscoverPresenter.POPULAR_PEOPLE_KEY, it as ArrayList<out Parcelable>)
        }
    }

    private fun fetchPopularPeople() {
        discoverClient.getPopularPeople(popularPeopleAsyncResponse)
    }
}