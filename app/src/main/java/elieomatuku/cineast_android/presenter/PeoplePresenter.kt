package elieomatuku.cineast_android.presenter

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.service.ContentManager
import elieomatuku.cineast_android.business.model.data.*
import elieomatuku.cineast_android.business.model.response.ImageResponse
import elieomatuku.cineast_android.business.model.response.PeopleCreditsResponse
import elieomatuku.cineast_android.vu.PeopleVu
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.generic.instance
import timber.log.Timber

class PeoplePresenter : BasePresenter<PeopleVu>() {
    companion object {
        const val PEOPLE_KEY = "people"
        const val SCREEN_NAME_KEY = "screen_name"
        const val PEOPLE_DETAILS_KEY = "people_details"
        const val PEOPLE_MOVIES_KEY = "people_movies"
        const val MOVIE_TEAM_KEY = "movie_team"
    }

    private val contentManager: ContentManager by App.kodein.instance()

    var peopleDetails: PeopleDetails? = null
    var peopleMovies: List<KnownFor>? = listOf()

    override fun onLink(vu: PeopleVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val screenName = args.getString(SCREEN_NAME_KEY)
        val people: Person = args.getParcelable(PEOPLE_KEY) as Person

        vu.personPresentedPublisher?.onNext(people)

        peopleDetails = inState?.getParcelable(PEOPLE_DETAILS_KEY)
        peopleMovies = inState?.getParcelableArrayList(PEOPLE_MOVIES_KEY)

        if (peopleDetails != null && peopleMovies != null) {
            vu.updateVu(peopleDetails, screenName, peopleMovies)
        } else {
            vu.showLoading()
            contentManager.getPeopleDetails(people, object : AsyncResponse<PeopleDetails> {
                override fun onSuccess(response: PeopleDetails?) {
                    peopleDetails = response
                    getPeopleMovies(people, peopleDetails, screenName)
                }

                override fun onFail(error: CineastError) {
                    Timber.e("error: $error")
                }
            })
        }

        rxSubs.add(vu.onProfileClickedPictureObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ peopleId ->

                    contentManager.getPeopleImages(peopleId, object : AsyncResponse<ImageResponse> {
                        override fun onSuccess(response: ImageResponse?) {
                            val poster = response?.peoplePosters
                            handler.post {
                                vu.goToGallery(poster)
                            }
                        }

                        override fun onFail(error: CineastError) {
                            Timber.d("error: $error")
                        }
                    })
                }))


    }

    private fun getPeopleMovies(actor: Person, peopleDetails: PeopleDetails?, screenName: String) {
        contentManager.getPeopleMovies(actor, object : AsyncResponse<PeopleCreditsResponse> {
            override fun onSuccess(response: PeopleCreditsResponse?) {
                peopleMovies = response?.cast as List<KnownFor>
                handler.post {
                    vu?.hideLoading()
                    vu?.updateVu(peopleDetails, screenName, peopleMovies)
                }
            }

            override fun onFail(error: CineastError) {
                handler.post {
                    //vu?.updateErrorView(error.status_message)
                }
            }
        })
    }

    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)

        peopleDetails?.let {
            outState.putParcelable(PEOPLE_DETAILS_KEY, it)
        }

        peopleMovies?.let {
            outState.putParcelableArrayList(PEOPLE_MOVIES_KEY, it as ArrayList<out Parcelable>)
        }
    }
}