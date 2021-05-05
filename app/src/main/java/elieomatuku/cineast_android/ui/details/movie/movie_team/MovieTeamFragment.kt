package elieomatuku.cineast_android.ui.details.movie.movie_team

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Cast
import elieomatuku.cineast_android.core.model.Crew
import elieomatuku.cineast_android.core.model.MovieSummary
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.databinding.FragmentOverviewBinding
import elieomatuku.cineast_android.ui.common_fragment.BaseFragment
import elieomatuku.cineast_android.ui.details.people.PeopleActivity
import elieomatuku.cineast_android.ui.details.people.PeoplePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber


class MovieTeamFragment : BaseFragment() {
    companion object {
        const val MOVIE_SUMMARY = "movie_summary"
        const val SCREEN_NAME_KEY = "screen_name"
        const val PEOPLE_KEY = "peopleApi"


        fun newInstance(movieSummary: MovieSummary): MovieTeamFragment {
            val args = Bundle()

            args.putParcelable(MOVIE_SUMMARY, movieSummary)

            val fragment = MovieTeamFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewDataBinding: FragmentOverviewBinding

    var movieTitle: String? = null

    private val onCrewSelectPublisher: PublishSubject<Crew> by lazy {
        PublishSubject.create<Crew>()
    }

    private val onCrewSelectObservable: Observable<Crew>
        get() = onCrewSelectPublisher.hide()

    private val onCastSelectPublisher: PublishSubject<Cast> by lazy {
        PublishSubject.create<Cast>()
    }

    private val onCastSelectObservable: Observable<Cast>
        get() = onCastSelectPublisher.hide()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_overview, container, false)
        viewDataBinding = FragmentOverviewBinding.bind(rootView)

        val movieSummary: MovieSummary? = arguments?.getParcelable<MovieSummary>(MOVIE_SUMMARY)

        val cast: List<Cast>? = movieSummary?.cast
        val crew: List<Crew>? = movieSummary?.crew
        movieTitle = movieSummary?.movie?.title


        if (cast != null && crew != null) {
            updateView(cast, crew)
        }

        return viewDataBinding.root

    }

    override fun onResume() {
        super.onResume()
        rxSubs.add(onCastSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPersonSelectedSuccess, this::onSelectionFail))

        rxSubs.add(onCrewSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPersonSelectedSuccess, this::onSelectionFail))
    }


    private fun updateView(cast: List<Cast>, crew: List<Crew>) {
        viewDataBinding.overviewList.adapter = MovieTeamAdapter(cast, crew, onCrewSelectPublisher, onCastSelectPublisher)
        viewDataBinding.overviewList.layoutManager = LinearLayoutManager(activity)
    }

    private fun onPersonSelectedSuccess(person: Person) {
        val params = Bundle()
        params.putString(SCREEN_NAME_KEY, movieTitle)
        params.putParcelable(PEOPLE_KEY, person)
        params.putBoolean(PeoplePresenter.MOVIE_TEAM_KEY, true)
        gotoPeople(params)
    }

    private fun onSelectionFail(t: Throwable) {
        Timber.d("movieSelectObservable failed:$t")
    }

    private fun gotoPeople(params: Bundle) {
        val intent = Intent(activity, PeopleActivity::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }

}