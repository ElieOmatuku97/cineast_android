package elieomatuku.cineast_android.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.viewholder.itemHolder.HeaderItemHolder
import io.reactivex.subjects.PublishSubject

class HeaderAdapter(private val movies: List<Movie>, private val onItemClickPublisher: PublishSubject<Movie>, private val itemListLayoutRes: Int? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
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
        val widgetPosition = movies.indexOfFirst {
            it.id  == key
        }

        return widgetPosition
    }
}