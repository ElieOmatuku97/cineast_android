package elieomatuku.cineast_android.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.model.data.*
import elieomatuku.cineast_android.viewholder.*
import elieomatuku.cineast_android.viewholder.itemHolder.LoginViewHolder
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import kotlin.properties.Delegates

class DiscoverAdapter(private val onMovieClickPublisher: PublishSubject<Movie>, private val onPersonalityClickPublisher: PublishSubject<Person>,
                      private val loginClickPublisher: PublishSubject<Boolean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val POSITION_HEADER = 0
        const val TYPE_HEADER = 0
        const val TYPE_POPULAR_MOVIE = 1
        const val TYPE_POPULAR_PEOPLE = 2
        const val TYPE_NOW_PLAYING_MOVIE = 3
        const val TYPE_UPCOMING_MOVIE = 4
        const val TYPE_TOP_RATED_MOVIE = 5
        const val TYPE_LOGIN = 6

        const val TYPE_EMPTY_STATE = -2
    }

    var content: List<Content> = listOf()
    var isLoggedIn: Boolean = false


    var hasValidData = false
        private set

    var filteredWidgets: MutableMap<Int, Pair<Int, List<Content>?>>  by Delegates.observable(mutableMapOf()) { prop, oldEdition, nuEdition ->

        Timber.d("widgets = $nuEdition")
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

    val hasEmptyState: Boolean
        // only display empty state after valid data is set
        get() = hasValidData && (filteredWidgets.isEmpty())

    private fun getDiscoverPosition(position: Int): Int {
        return position - 1
    }

    var currentMovieId: Int = -1

    override fun getItemCount(): Int {

        Timber.d("hasEmptyState: ${hasEmptyState},  ${filteredWidgets.size}")

        return if (!hasEmptyState) {
            filteredWidgets.size + 2
        } else {
            1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasEmptyState) {
            Timber.d("empty state update function called")

            TYPE_EMPTY_STATE
        } else {
            when (position) {
                POSITION_HEADER -> TYPE_HEADER
                TYPE_POPULAR_MOVIE -> TYPE_POPULAR_MOVIE
                TYPE_POPULAR_PEOPLE -> TYPE_POPULAR_PEOPLE
                TYPE_NOW_PLAYING_MOVIE -> TYPE_NOW_PLAYING_MOVIE
                TYPE_UPCOMING_MOVIE -> TYPE_UPCOMING_MOVIE
                TYPE_TOP_RATED_MOVIE -> TYPE_TOP_RATED_MOVIE
                TYPE_LOGIN -> TYPE_LOGIN
                else -> -1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                HeaderHolder.newInstance(parent)
            }

            TYPE_POPULAR_PEOPLE -> {
                PopularPeopleHolder.newInstance(parent, onPersonalityClickPublisher)
            }

            TYPE_POPULAR_MOVIE, TYPE_NOW_PLAYING_MOVIE, TYPE_UPCOMING_MOVIE, TYPE_TOP_RATED_MOVIE -> {
                MovieHolder.newInstance(parent, onMovieClickPublisher)
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
            is HeaderHolder -> {
                val widget = filteredWidgets[TYPE_HEADER]
                val movies = widget?.second as List<Movie>
                holder.update(movies, onMovieClickPublisher, currentMovieId)
            }

            is MovieHolder -> {
                val widget = filteredWidgets[getDiscoverPosition(position)]
                val movies = widget?.second as List<Movie>
                val widgetTitle = widget.first
                holder.update(movies, widgetTitle)
            }

            is PopularPeopleHolder -> {
                val widget = filteredWidgets[getDiscoverPosition(position)]
                val personalities = widget?.second as List<Personality>
                holder.update(personalities)
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