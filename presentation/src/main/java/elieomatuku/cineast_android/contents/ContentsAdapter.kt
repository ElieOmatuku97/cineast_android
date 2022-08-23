package elieomatuku.cineast_android.contents

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.viewholder.EmptyStateHolder
import elieomatuku.cineast_android.viewholder.MovieItemHolder
import elieomatuku.cineast_android.viewholder.PeopleItemHolder
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import kotlin.properties.Delegates

open class ContentsAdapter(
    private val onContentClickPublisher: PublishSubject<Content>,
    private val contentLayoutRes: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_EMPTY_STATE = -2
        const val TYPE_CONTENT = 1
    }

    var contents: MutableList<Content> by Delegates.observable(mutableListOf()) { prop, oldContents, nuContents ->
        hasValidData = true
        errorMessage = null
    }

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
        get() = hasValidData && (contents.isEmpty())

    override fun getItemCount(): Int {
        Timber.d("hasEmptyState: $hasEmptyState")
        return if (hasEmptyState) {
            1
        } else {
            contents.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasEmptyState) {
            TYPE_EMPTY_STATE
        } else {
            TYPE_CONTENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY_STATE -> {
                EmptyStateHolder.newInstance(parent)
            }

            TYPE_CONTENT -> {
                if (contents.first() is Movie) {
                    MovieItemHolder.newInstance(parent, contentLayoutRes)
                } else {
                    PeopleItemHolder.newInstance(parent, contentLayoutRes)
                }
            }

            else -> throw RuntimeException("View Type does not exist.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieItemHolder -> {
                val movie = contents[position]
                holder.update(movie as Movie)

                holder.itemView.setOnClickListener {
                    onContentClickPublisher.onNext(contents[position])
                }
            }

            is PeopleItemHolder -> {
                val people = contents[position]

                people.let {
                    holder.update(people as Person)
                }

                holder.itemView.setOnClickListener {
                    onContentClickPublisher.onNext(people)
                }
            }

            is EmptyStateHolder -> {
                holder.update(errorMessage)
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is EmptyStateHolder -> holder.composeView.disposeComposition()
            else -> {}
        }
    }
}
