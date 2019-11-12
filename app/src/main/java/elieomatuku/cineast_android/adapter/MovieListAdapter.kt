package elieomatuku.cineast_android.adapter

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.viewholder.EmptyStateHolder
import elieomatuku.cineast_android.viewholder.itemHolder.MovieItemHolder
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import kotlin.properties.Delegates


open class MovieListAdapter(private val onItemClickPublisher: PublishSubject<Movie>,
                            private val itemListLayoutRes: Int? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_EMPTY_STATE = -2
        const val TYPE_MOVIE = 1

    }

    var movies: MutableList<Movie> by Delegates.observable(mutableListOf()) { prop, oldMovies, nuMovies ->

        Timber.d("widgets = $nuMovies")
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
        get() = hasValidData && (movies.isEmpty())

    override fun getItemCount(): Int {
        Timber.d("hasEmptyState: $hasEmptyState")
        return if (hasEmptyState) {
            1
        } else {
            movies.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasEmptyState) {
            TYPE_EMPTY_STATE
        } else {
            TYPE_MOVIE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY_STATE -> {
                EmptyStateHolder.newInstance(parent)
            }

            TYPE_MOVIE -> {
                MovieItemHolder.newInstance(parent, itemListLayoutRes)
            }

            else -> throw RuntimeException("View Type does not exist.")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {

            is MovieItemHolder -> {

                val movie = movies[position]
                holder.update(movie)

                holder.itemView.setOnClickListener {
                    Log.d(MovieListAdapter::class.java.simpleName, "CLICKED && movie:  ${movies[position]}")
                    onItemClickPublisher.onNext(movies[position])
                }
            }

            is EmptyStateHolder -> {
                holder.update()
            }
        }

    }

}