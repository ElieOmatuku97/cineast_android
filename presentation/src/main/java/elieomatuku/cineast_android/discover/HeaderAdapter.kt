package elieomatuku.cineast_android.discover

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.domain.model.Movie
import io.reactivex.subjects.PublishSubject

class HeaderAdapter(private val movies: List<Movie>, private val onItemClickPublisher: PublishSubject<Movie>, private val itemListLayoutRes: Int? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HeaderItemHolder.newInstance(parent, itemListLayoutRes)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val headerItemHolder = holder as HeaderItemHolder
        headerItemHolder.update(movies[position])

        headerItemHolder.imageView.setOnClickListener {
            onItemClickPublisher.onNext(movies[position])
        }
    }

    fun getArticlePosition(key: Int): Int {
        return movies.indexOfFirst {
            it.id == key
        }
    }
}
