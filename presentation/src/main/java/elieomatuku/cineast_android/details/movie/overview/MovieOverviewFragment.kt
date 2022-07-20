package elieomatuku.cineast_android.details.movie.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.databinding.FragmentMovieOverviewBinding
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.details.BareOverviewFragment
import elieomatuku.cineast_android.fragment.YoutubeFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MovieOverviewFragment(private val bareOverviewFragment: Fragment) : BaseFragment() {
    companion object {
        private const val OVERVIEW_SUMMARY = "overview_movie_summary"

        fun newInstance(overviewTitle: String, movieSummary: MovieSummary): MovieOverviewFragment {
            val args = Bundle()
            args.putSerializable(OVERVIEW_SUMMARY, movieSummary)

            val fragment =
                MovieOverviewFragment(
                    BareOverviewFragment.newInstance(
                        overviewTitle,
                        movieSummary.movie?.overview
                    )
                )
            fragment.arguments = args

            return fragment
        }
    }

    private var _binding: FragmentMovieOverviewBinding? = null
    private val binding get() = _binding!!

    private val onTrailClickPublisher: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }
    private val onTrailClickObservable: Observable<String>
        get() = onTrailClickPublisher.hide()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieSummary: MovieSummary? = arguments?.get(OVERVIEW_SUMMARY) as MovieSummary?

        binding.overviewList.adapter = MovieOverviewAdapter(movieSummary, onTrailClickPublisher)
        binding.overviewList.layoutManager = LinearLayoutManager(this.context)

        childFragmentManager.beginTransaction().add(R.id.bareOverView, bareOverviewFragment)
            .addToBackStack(null).commit()
    }

    override fun onResume() {
        super.onResume()
        rxSubs.add(
            onTrailClickObservable
                .subscribe {
                    showTrailer(it)
                }
        )
    }

    private fun showTrailer(trailerKey: String) {
        val youtubeFragment = YoutubeFragment.newInstance(trailerKey)
        val fm = (context as AppCompatActivity).supportFragmentManager
        fm.beginTransaction().add(android.R.id.content, youtubeFragment, null)
            .addToBackStack(null).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
