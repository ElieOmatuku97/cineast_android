package elieomatuku.restapipractice.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.restapipractice.business.business.model.data.Movie
import elieomatuku.restapipractice.viewholder.itemHolder.HeaderItemHolder
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