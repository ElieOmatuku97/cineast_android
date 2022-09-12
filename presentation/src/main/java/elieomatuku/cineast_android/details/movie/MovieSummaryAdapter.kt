package elieomatuku.cineast_android.details.movie

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.viewholder.EmptyStateHolder
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates

class MovieSummaryAdapter(
    private val onProfileClickedPicturePublisher: PublishSubject<Int>,
    private val segmentedButtonsPublisher: PublishSubject<Pair<String, MovieSummary>>,
    private val onProfileLinkClickedPublisher: PublishSubject<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_MOVIE_PROFILE = 0
        const val TYPE_MENU_MOVIE = 1
        const val TYPE_EMPTY_STATE = -2
    }

    var movieSummary: MovieSummary by Delegates.observable(MovieSummary()) { _, _, _ ->
        hasValidData = true
        errorMessage = null
    }

    private var initialCheckedTab: String = MovieFragment.MOVIE_OVERVIEW

    var hasValidData = false
        private set

    private var _errorMessage: String? = null
    var errorMessage: String?
        get() = _errorMessage
        set(nuErrorMessage) {
            _errorMessage = nuErrorMessage
            hasValidData = true
        }

    private val hasEmptyState: Boolean
        // only display empty state after valid data is set
        get() = hasValidData && (movieSummary.isEmpty())

    override fun getItemCount(): Int {
        return if (hasEmptyState) {
            1
        } else {
            2
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasEmptyState) {
            TYPE_EMPTY_STATE
        } else {
            when (position) {
                TYPE_MOVIE_PROFILE -> TYPE_MOVIE_PROFILE
                TYPE_MENU_MOVIE -> TYPE_MENU_MOVIE
                else -> -1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MOVIE_PROFILE -> MovieProfileHolder.newInstance(
                parent,
                onProfileClickedPicturePublisher,
                onProfileLinkClickedPublisher
            )
            TYPE_MENU_MOVIE -> MovieSegmentedButtonHolder.newInstance(parent)
            TYPE_EMPTY_STATE -> EmptyStateHolder.newInstance(parent)
            else -> throw RuntimeException("View Type does not exist.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieProfileHolder) {
            holder.update(movieSummary)
        }

        if (holder is MovieSegmentedButtonHolder) {
            holder.update(movieSummary, initialCheckedTab)

            holder.overviewSegmentBtn.setOnClickListener {
                segmentedButtonsPublisher.onNext(Pair(MovieFragment.MOVIE_OVERVIEW, movieSummary))
            }

            holder.peopleSegmentBtn.setOnClickListener {
                segmentedButtonsPublisher.onNext(Pair(MovieFragment.MOVIE_CREW, movieSummary))
            }

            holder.similarSegmentBtn.setOnClickListener {
                segmentedButtonsPublisher.onNext(Pair(MovieFragment.SIMILAR_MOVIES, movieSummary))
            }
        }

        if (holder is EmptyStateHolder) {
            holder.update(errorMessage)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is EmptyStateHolder -> holder.composeView.disposeComposition()
            else -> {}
        }
    }
}
