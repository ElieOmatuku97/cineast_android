package elieomatuku.cineast_android.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.ui.viewholder.EmptyStateHolder
import elieomatuku.cineast_android.ui.viewholder.PopularPeopleItemHolder
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import kotlin.properties.Delegates

class PeopleAdapter(
    private val onItemClickPublisher: PublishSubject<Person>,
    private val itemListLayoutRes: Int? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_EMPTY_STATE = -2
        const val TYPE_PEOPLE = 1
    }

    var people: MutableList<Person> by Delegates.observable(mutableListOf()) { prop, oldEdition, nuEdition ->
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
        get() = hasValidData && (people.isEmpty())

    override fun getItemCount(): Int {
        Timber.d("hasEmptyState: $hasEmptyState")

        return if (hasEmptyState) {
            1
        } else {
            people.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasEmptyState) {
            TYPE_EMPTY_STATE
        } else {
            TYPE_PEOPLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY_STATE -> {
                EmptyStateHolder.newInstance(parent)
            }

            TYPE_PEOPLE -> {
                PopularPeopleItemHolder.newInstance(parent, itemListLayoutRes)
            }

            else -> throw RuntimeException("View Type does not exist.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PopularPeopleItemHolder -> {
                holder.update(people[position])

                holder.itemView.setOnClickListener {
                    onItemClickPublisher.onNext(people[position])
                }
            }

            is EmptyStateHolder -> {
                holder.update(errorMessage)
            }
        }
    }
}
