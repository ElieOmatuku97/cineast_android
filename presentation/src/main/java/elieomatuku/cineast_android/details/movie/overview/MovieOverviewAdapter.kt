package elieomatuku.cineast_android.details.movie.overview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.domain.model.MovieSummary
import io.reactivex.subjects.PublishSubject

class MovieOverviewAdapter(
    private val movieSummary: MovieSummary?,
    private val onTrailClickedPublisher: PublishSubject<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TRAILERS = 0
        const val TYPE_MOVIE_FACTS = 1
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            TYPE_TRAILERS -> TYPE_TRAILERS
            TYPE_MOVIE_FACTS -> TYPE_MOVIE_FACTS
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TRAILERS -> TrailersHolder.newInstance(parent, onTrailClickedPublisher)
            TYPE_MOVIE_FACTS -> MovieFactsHolder.newInstance(parent)
            else -> throw RuntimeException("View Type does not exist.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
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
