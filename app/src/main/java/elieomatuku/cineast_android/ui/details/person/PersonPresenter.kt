package elieomatuku.cineast_android.ui.details.person

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.business.api.response.ImageResponse
import elieomatuku.cineast_android.business.api.response.PeopleCreditsResponse
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.domain.model.*
import elieomatuku.cineast_android.ui.base.BasePresenter
import elieomatuku.cineast_android.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

class PersonPresenter : BasePresenter<PersonVu>() {
    companion object {
        const val PEOPLE_KEY = "peopleApi"
        const val PEOPLE_DETAILS_KEY = "people_details"
        const val PEOPLE_MOVIES_KEY = "people_movies"
        const val MOVIE_TEAM_KEY = "movie_team"
    }

    var personDetails: PersonDetails? = null
    var peopleMovies: List<Movie>? = listOf()

    override fun onLink(vu: PersonVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val screenName = args.getString(Constants.SCREEN_NAME_KEY)
        val people: Person? = args.getSerializable(PEOPLE_KEY) as Person?

        people?.let {
            vu.personPresentedPublisher?.onNext(people)
        }

        personDetails = inState?.getSerializable(PEOPLE_DETAILS_KEY) as PersonDetails?
        peopleMovies = inState?.getSerializable(PEOPLE_MOVIES_KEY) as List<Movie>?

        if (personDetails != null && peopleMovies != null) {
            vu.updateVu(personDetails, screenName, peopleMovies)
        } else {
            vu.showLoading()

            people?.let {
                contentService.getPeopleDetails(
                    people,
                    object : AsyncResponse<PersonDetails> {
                        override fun onSuccess(response: PersonDetails?) {
                            personDetails = response
                            if (screenName != null) {
                                getPeopleMovies(people, personDetails, screenName)
                            }
                        }

                        override fun onFail(error: CineastError) {
                            Timber.e("error: $error")
                            handler.post {
                                vu.hideLoading()
                                vu.updateErrorView(error.statusMessage)
                            }
                        }
                    }
                )
            }
        }

        rxSubs.add(
            vu.onProfileClickedPictureObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { peopleId ->

                    contentService.getPeopleImages(
                        peopleId,
                        object : AsyncResponse<ImageResponse> {
                            override fun onSuccess(response: ImageResponse?) {
                                val poster = response?.peoplePosters
                                handler.post {
                                    vu.goToGallery(poster)
                                }
                            }

                            override fun onFail(error: CineastError) {
                                Timber.d("error: $error")
                            }
                        }
                    )
                }
        )

        rxSubs.add(
            vu.onSegmentedButtonsObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    vu.gotoTab(it)
                }
        )
    }

    private fun getPeopleMovies(actor: Person, personDetails: PersonDetails?, screenName: String) {
        contentService.getPeopleMovies(
            actor,
            object : AsyncResponse<PeopleCreditsResponse> {
                override fun onSuccess(response: PeopleCreditsResponse?) {
                    peopleMovies = response?.cast as List<Movie>
                    handler.post {
                        vu?.hideLoading()
                        vu?.updateVu(personDetails, screenName, peopleMovies)
                    }
                }

                override fun onFail(error: CineastError) {
                    handler.post {
                        vu?.hideLoading()
                        vu?.updateErrorView(error.statusMessage)
                    }
                }
            }
        )
    }

    override fun onSaveState(outState: Bundle) {
        super.onSaveState(outState)

        personDetails?.let {
            outState.putSerializable(PEOPLE_DETAILS_KEY, it)
        }

        peopleMovies?.let {
            outState.putSerializable(PEOPLE_MOVIES_KEY, it as ArrayList<out Parcelable>)
        }
    }
}
