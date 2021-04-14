package elieomatuku.cineast_android.ui.details.movie.overview

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.core.model.MovieSummary

class OverviewAdapter(private val movieSummary: MovieSummary?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_PLOT_SUMMARY = 0
        const val TYPE_TRAILERS = 1
        const val TYPE_MOVIE_FACTS = 2
        const val TYPE_BOTTOM = 3
    }


    override fun getItemCount(): Int {
        return 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            TYPE_PLOT_SUMMARY -> TYPE_PLOT_SUMMARY
            TYPE_TRAILERS -> TYPE_TRAILERS
            TYPE_MOVIE_FACTS -> TYPE_MOVIE_FACTS
            TYPE_BOTTOM -> TYPE_BOTTOM
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PLOT_SUMMARY -> PlotSummaryHolder.newInstance(parent)
            TYPE_TRAILERS -> TrailersHolder.newInstance(parent)
            TYPE_MOVIE_FACTS -> MovieFactsHolder.newInstance(parent)
            TYPE_BOTTOM -> BottomHolder.newInstance(parent)
            else -> throw RuntimeException("View Type does not exist.")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            TYPE_PLOT_SUMMARY -> {
                val plotSummaryHolder = holder as PlotSummaryHolder
                val overView = movieSummary?.movie?.overview
                plotSummaryHolder.update(overView)
            }

            TYPE_TRAILERS -> {
                val trailersHolder = holder as TrailersHolder
                val trailers = movieSummary?.trailers

                if (trailers != null) {
                    trailersHolder.update(trailers)
                }
            }

            TYPE_MOVIE_FACTS -> {
                val movieFactsHolder = holder as MovieFactsHolder
                val movieFacts = movieSummary?.facts
                movieFactsHolder.update(movieFacts)
            }
        }
    }
}
