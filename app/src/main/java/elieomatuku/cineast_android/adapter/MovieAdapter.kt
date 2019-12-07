package elieomatuku.cineast_android.adapter

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.fragment.OverviewFragment
import elieomatuku.cineast_android.fragment.MovieTeamFragment
import elieomatuku.cineast_android.fragment.SimilarMovieFragment
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.model.data.*
import elieomatuku.cineast_android.viewholder.EmptyStateHolder
import elieomatuku.cineast_android.viewholder.itemHolder.*
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import kotlin.properties.Delegates

class MovieAdapter(private val onProfileClickedPicturePublisher: PublishSubject<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_MOVIE_PROFILE = 0
        const val TYPE_MENU_MOVIE = 1
        const val TYPE_EMPTY_STATE = -2
    }


    var movieSummary: MovieSummary by Delegates.observable(MovieSummary()) { prop, oldMovieSummary, nuMovieSummary ->
        Timber.d("summary = $nuMovieSummary")
        hasValidData = true
        errorMessage = null
    }


    var hasValidData = false
        private set


    private var _errorMessage: String? = null
    var errorMessage: String?
        get() = _errorMessage
        set(nuErrorMessage) {
            Timber.d("from MovieListAdapter: $nuErrorMessage")
            _errorMessage = nuErrorMessage
            hasValidData = true
        }

    val hasEmptyState: Boolean
        // only display empty state after valid data is set
        get() = hasValidData && (movieSummary.isEmpty())

    override fun getItemCount(): Int {
        Timber.d("hasEmptyState: $hasEmptyState")
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
            TYPE_MOVIE_PROFILE -> ProfileMovieHolder.newInstance(parent, onProfileClickedPicturePublisher)
            TYPE_MENU_MOVIE -> MenuMovieHolder.newInstance(parent)
            TYPE_EMPTY_STATE -> {
                EmptyStateHolder.newInstance(parent)
            }
            else -> throw RuntimeException("View Type does not exist.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            TYPE_MOVIE_PROFILE -> {
                val movieProfileHolder = holder as ProfileMovieHolder
                movieProfileHolder.update(movieSummary)
            }

            TYPE_MENU_MOVIE -> {
                val menuMovieHolder = holder as MenuMovieHolder

                menuMovieHolder.itemView.visibility = if (movieSummary.movie != null) View.VISIBLE else View.GONE

                menuMovieHolder.overviewSegmentBtn.setOnClickListener {
                    val activity = it.context as FragmentActivity
                    val overviewFragment = OverviewFragment.newInstance(movieSummary)
                    (activity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, overviewFragment).commit()
                }

                menuMovieHolder.peopleSegmentBtn.setOnClickListener {
                    val activity = it.context as FragmentActivity
                    val peopleFragment = MovieTeamFragment.newInstance(movieSummary)
                    (activity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, peopleFragment).commit()
                }

                menuMovieHolder.similarSegmentBtn.setOnClickListener {
                    val activity = it.context as FragmentActivity
                    val similarFragment = SimilarMovieFragment.newInstance(movieSummary)
                    (activity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, similarFragment).commit()
                }
            }

            TYPE_EMPTY_STATE -> {
                (holder as EmptyStateHolder).update(errorMessage)
            }

        }
    }
}
