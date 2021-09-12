package elieomatuku.cineast_android.ui.discover

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.extensions.Contents
import elieomatuku.cineast_android.ui.viewholder.ContentHolder
import elieomatuku.cineast_android.ui.viewholder.EmptyStateHolder
import elieomatuku.cineast_android.ui.viewholder.PeopleHolder
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates

class DiscoverAdapter(
    private val onMovieClickPublisher: PublishSubject<Movie>,
    private val onPersonClickPublisher: PublishSubject<Content>,
    private val loginClickPublisher: PublishSubject<Boolean>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_MOVIES = 1
        const val TYPE_POPULAR_PEOPLE = 2
        const val TYPE_LOGIN = 6
        const val TYPE_EMPTY_STATE = -2

        const val POSITION_HEADER = 0
        const val POSITION_POPULAR_MOVIE = 1
        const val POSITION_POPULAR_PEOPLE = 2
        const val POSITION_NOW_PLAYING_MOVIE = 3
        const val POSITION_UPCOMING_MOVIE = 4
        const val POSITION_TOP_RATED_MOVIE = 5
    }

    var isLoggedIn: Boolean = false

    var hasValidData = false
        private set

    var filteredContents: MutableMap<Int, Contents> by Delegates.observable(mutableMapOf()) { prop, oldDiscoverContent, nuDiscoverContent ->
        hasValidData = true
        errorMessage = null
    }

    private var _errorMessage: String? = null
    var errorMessage: String?
        get() = _errorMessage
        set(nuErrorMessage) {
            _errorMessage = nuErrorMessage
            hasValidData = true
        }

    private val hasEmptyState: Boolean
        // only display empty state after valid data is set
        get() = hasValidData && (filteredContents.isEmpty())

    private fun getDiscoverPosition(position: Int): Int {
        return if (position == POSITION_HEADER) {
            POSITION_HEADER
        } else {
            position - 1
        }
    }

    override fun getItemCount(): Int {
        return if (!hasEmptyState) {
            filteredContents.size + 2
        } else {
            1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasEmptyState) {
            TYPE_EMPTY_STATE
        } else {
            when (position) {
                POSITION_HEADER -> TYPE_HEADER
                POSITION_POPULAR_MOVIE, POSITION_TOP_RATED_MOVIE, POSITION_UPCOMING_MOVIE, POSITION_NOW_PLAYING_MOVIE -> TYPE_MOVIES
                POSITION_POPULAR_PEOPLE -> TYPE_POPULAR_PEOPLE
                TYPE_LOGIN -> TYPE_LOGIN
                else -> -1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                HeaderHolder.newInstance(parent, onMovieClickPublisher)
            }

            TYPE_POPULAR_PEOPLE -> {
                PeopleHolder.newInstance(parent, onPersonClickPublisher)
            }

            TYPE_MOVIES -> {
                MovieHolder.newInstance(parent)
            }

            TYPE_LOGIN -> {
                LoginViewHolder.newInstance(parent)
            }

            TYPE_EMPTY_STATE -> {
                EmptyStateHolder.newInstance(parent)
            }
            else -> throw RuntimeException("View Type does not exist.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PeopleHolder -> {
                val contents = filteredContents[getDiscoverPosition(position)]
                contents?.let {
                    holder.update(it)
                }
            }

            is ContentHolder -> {
                val contents = filteredContents[getDiscoverPosition(position)]
                contents?.let {
                    holder.update(contents)
                }
            }

            is LoginViewHolder -> {
                holder.itemView.setOnClickListener {
                    loginClickPublisher.onNext(true)
                }
                holder.update(isLoggedIn)
            }

            is EmptyStateHolder -> {
                holder.update(errorMessage)
            }
        }
    }
}
