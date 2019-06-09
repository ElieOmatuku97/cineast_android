package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.business.service.RestService
import elieomatuku.cineast_android.business.business.service.DiscoverService
import elieomatuku.cineast_android.business.business.model.data.*
import elieomatuku.cineast_android.business.business.model.response.ImageResponse
import elieomatuku.cineast_android.business.business.model.response.PeopleCreditsResponse
import elieomatuku.cineast_android.vu.PeopleVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class PeoplePresenter: BasePresenter<PeopleVu>() {
    companion object {
        const val PEOPLE_KEY = "people"
        const val SCREEN_NAME_KEY = "screen_name"
        const val PEOPLE_DETAILS_KEY = "people_details"
        const val PEOPLE_MOVIES_KEY = "people_movies"
        const val MOVIE_TEAM_KEY = "movie_team"
    }

    private val restService: RestService by App.kodein.instance()

    var peopleDetails : PeopleDetails? = null
    var peopleMovies: List<PeopleCast>? =  listOf()

    override fun onLink(vu: PeopleVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val screenName = args.getString(SCREEN_NAME_KEY)
        val people: Person = args.getParcelable(PEOPLE_KEY) as Person

        vu.personPresentedPublisher?.onNext(people)

        peopleDetails = inState?.getParcelable(PEOPLE_DETAILS_KEY)
        peopleMovies = inState?.getParcelableArrayList(PEOPLE_MOVIES_KEY)

        if (peopleDetails != null  && peopleMovies != null) {
            vu.updateVu(peopleDetails, screenName, peopleMovies)
        } else {
            vu.showLoading()
            val id: Int?= people.id

            if (id != null) {
                restService.peopleApi.getPeopleDetails(id, DiscoverService.API_KEY).enqueue(object : Callback<PeopleDetails> {
                    override fun onResponse(call: Call<PeopleDetails>?, response: Response<PeopleDetails>?) {
                        peopleDetails = response?.body()
                        getPeopleMovies(id, peopleDetails, screenName)
                    }

                    override fun onFailure(call: Call<PeopleDetails>?, t: Throwable?) {
                        Timber.e("error: $t")
                    }
                })
            }
        }

        rxSubs.add(vu.onProfileClickedPictureObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe ({peopleId ->
                    restService.peopleApi.getPeopleImages(peopleId, DiscoverService.API_KEY).enqueue( object : Callback<ImageResponse>{
                        override fun onResponse(call: Call<ImageResponse>?, response: Response<ImageResponse>?) {
                            val poster = response?.body()?.peoplePosters
                            handler.post {
                                vu.goToGallery(poster)
                            }
                        }

                        override fun onFailure(call: Call<ImageResponse>?, t: Throwable?) {
                            Timber.d("error: $t")
                        }
                    })
                })
        )
    }

    private fun getPeopleMovies(actorID: Int ,peopleDetails: PeopleDetails?, screenName: String ) {
        restService.peopleApi.getPeopleCredits(actorID, DiscoverService.API_KEY).enqueue(object: Callback<PeopleCreditsResponse> {
            override fun onResponse(call: Call<PeopleCreditsResponse>?, response: Response<PeopleCreditsResponse>?) {
                peopleMovies = response?.body()?.cast as List<PeopleCast>
                handler.post {
                    vu?.hideLoading()
                    vu?.updateVu(peopleDetails, screenName, peopleMovies)
                }
            }
            override fun onFailure(call: Call<PeopleCreditsResponse>?, t: Throwable?) {
                Timber.d( "error: $t")
            }
        })
    }

    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)

        peopleDetails?.let {
            outState.putParcelable(PEOPLE_DETAILS_KEY, it)
        }

        peopleMovies?.let{
            outState.putParcelableArrayList(PEOPLE_MOVIES_KEY, it as ArrayList<out Parcelable>)
        }
    }
}