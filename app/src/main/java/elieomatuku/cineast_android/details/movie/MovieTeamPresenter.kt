package elieomatuku.cineast_android.details.movie

import android.os.Bundle
import elieomatuku.cineast_android.core.model.Cast
import elieomatuku.cineast_android.core.model.Crew
import elieomatuku.cineast_android.core.model.MovieSummary
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.presenter.BasePresenter
import elieomatuku.cineast_android.details.people.PeoplePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber


class MovieTeamPresenter : BasePresenter<MovieTeamVu>() {
    companion object {
        const val SCREEN_NAME_KEY = "screen_name"
        const val PEOPLE_KEY = "peopleApi"
    }


    var movieTitle: String? = null

    override fun onLink(vu: MovieTeamVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val movieSummary: MovieSummary = args.get(MovieTeamFragment.MOVIE_SUMMARY) as MovieSummary

        val cast: List<Cast>? = movieSummary.cast
        val crew: List<Crew>? = movieSummary.crew
        movieTitle = movieSummary.movie?.title


        if (cast != null && crew != null) {
            vu.updateVu(cast, crew)
        }

        rxSubs.add(vu.onCastSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPersonSelectedSuccess, this::onSelectionFail))

        rxSubs.add(vu.onCrewSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPersonSelectedSuccess, this::onSelectionFail))
    }

    private fun onPersonSelectedSuccess(person: Person) {
        val params = Bundle()
        params.putString(SCREEN_NAME_KEY, movieTitle)
        params.putParcelable(PEOPLE_KEY, person)
        params.putBoolean(PeoplePresenter.MOVIE_TEAM_KEY, true)
        vu?.gotoPeople(params)
    }

    private fun onSelectionFail(t: Throwable) {
        Timber.d("movieSelectObservable failed:$t")
    }
}