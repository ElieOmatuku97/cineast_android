package elieomatuku.cineast_android.details.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.OverviewAdapter
import elieomatuku.cineast_android.core.model.MovieSummary
import kotlinx.android.synthetic.main.fragment_overview.view.*


class OverviewFragment : Fragment() {
    companion object {
        const val OVERVIEW_SUMMARY = "overview_summary"

        fun newInstance(movieSummary: MovieSummary): OverviewFragment {
            val args = Bundle()
            args.putParcelable(OVERVIEW_SUMMARY, movieSummary)

            val fragment = OverviewFragment()
            fragment.arguments = args

            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = LayoutInflater.from(this.context).inflate(R.layout.fragment_overview, container, false)
        val movieSummary: MovieSummary? = arguments?.get(OVERVIEW_SUMMARY) as MovieSummary?

        val overviewListView = rootView.overview_list
        overviewListView.adapter = OverviewAdapter(movieSummary)
        overviewListView.layoutManager = LinearLayoutManager(this.context)
        return rootView
    }
}