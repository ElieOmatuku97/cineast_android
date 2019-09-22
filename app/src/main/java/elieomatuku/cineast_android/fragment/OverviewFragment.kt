package elieomatuku.cineast_android.fragment

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.OverviewAdapter
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.model.data.MovieDetails
import elieomatuku.cineast_android.business.model.data.MovieSummary
import elieomatuku.cineast_android.business.model.data.Trailer
import kotlinx.android.synthetic.main.fragment_overview.view.*
import java.util.ArrayList

class OverviewFragment: Fragment(){
    companion object {
        const val OVERVIEW_MOVIE = "overview_movie"
        const val OVERVIEW_TRAILERS = "overview_trailers"
        const val OVERVIEW_MOVIE_DETAILS  = "overview_movieDetails"

        fun newInstance(movieSummary: MovieSummary): OverviewFragment {
            val args = Bundle()
            val movie = movieSummary.movie
            args.putParcelable(OVERVIEW_MOVIE, movie)

            val trailers = movieSummary.trailers
            args.putParcelableArrayList(OVERVIEW_TRAILERS, trailers as ArrayList<out Parcelable>)

            val details = movieSummary.details
            args.putParcelable(OVERVIEW_MOVIE_DETAILS, details)

            val fragment = OverviewFragment()
            fragment.arguments = args

            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val movie: Movie = arguments?.get(OVERVIEW_MOVIE) as Movie
        val trailers = arguments?.get(OVERVIEW_TRAILERS) as List <Trailer>
        val movieDetails: MovieDetails = arguments?.get(OVERVIEW_MOVIE_DETAILS) as MovieDetails
        val rootView = LayoutInflater.from(this.context).inflate(R.layout.fragment_overview, container,false)

        val overviewListView = rootView.overview_list
        overviewListView.adapter = OverviewAdapter (movie, trailers, movieDetails)
        overviewListView.layoutManager = LinearLayoutManager(this.context)
        return rootView
    }
}