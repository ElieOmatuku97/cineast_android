package elieomatuku.cineast_android.ui.details.movie.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.MovieSummary
import elieomatuku.cineast_android.ui.details.people.overview.BareOverviewFragment
import kotlinx.android.synthetic.main.fragment_overview.view.*


class MetaOverviewFragment(private val bareOverviewFragment: Fragment) : Fragment() {
    companion object {
        const val OVERVIEW_SUMMARY = "overview_summary"

        fun newInstance(overviewTitle: String, movieSummary: MovieSummary): MetaOverviewFragment {
            val args = Bundle()
            args.putParcelable(OVERVIEW_SUMMARY, movieSummary)

            val fragment =
                MetaOverviewFragment(BareOverviewFragment.newInstance(overviewTitle, movieSummary.movie?.overview))
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            LayoutInflater.from(this.context).inflate(R.layout.fragment_overview, container, false)
        val movieSummary: MovieSummary? = arguments?.get(OVERVIEW_SUMMARY) as MovieSummary?


        val overviewListView = rootView.overview_list
        overviewListView.adapter = OverviewAdapter(movieSummary)
        overviewListView.layoutManager = LinearLayoutManager(this.context)

        childFragmentManager.beginTransaction().add(R.id.bareOverView, bareOverviewFragment)
            .addToBackStack(null).commit()


        return rootView
    }
}
