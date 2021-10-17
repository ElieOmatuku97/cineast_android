package elieomatuku.cineast_android.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.ui.extensions.Contents
import elieomatuku.cineast_android.ui.viewholder.ContentHolder
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_header.view.*

class HeaderHolder(itemView: View, private val onItemClickPublisher: PublishSubject<Movie>) :
    ContentHolder, RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_header, parent, false)
        }

        fun newInstance(
            parent: ViewGroup,
            onItemClickPublisher: PublishSubject<Movie>
        ): HeaderHolder {
            return HeaderHolder(createView(parent), onItemClickPublisher)
        }

        const val CURRENT_MOVIE_ID = -1
    }

    private val listView: RecyclerView by lazy {
        itemView.header_popular_movie
    }

    private val smoothScroller: RecyclerView.SmoothScroller by lazy {
        object : LinearSmoothScroller(itemView.context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    }

    override fun update(content: Contents) {
        val adapter = HeaderAdapter(content.value as List<Movie>, onItemClickPublisher)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
        listView.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        listView.visibility = View.VISIBLE

        val lm = listView.layoutManager
        val position = adapter.getArticlePosition(CURRENT_MOVIE_ID)

        if ((position >= 0) && (position < adapter.itemCount)) {
            smoothScroller.targetPosition = position
            listView.post {
                lm?.startSmoothScroll(smoothScroller)
            }
        }
    }

    override fun update(content: List<Content>, titleRes: Int) {
        val adapter = HeaderAdapter(content as List<Movie>, onItemClickPublisher)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
        listView.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        listView.visibility = View.VISIBLE

        val lm = listView.layoutManager
        val position = adapter.getArticlePosition(CURRENT_MOVIE_ID)

        if ((position >= 0) && (position < adapter.itemCount)) {
            smoothScroller.targetPosition = position
            listView.post {
                lm?.startSmoothScroll(smoothScroller)
            }
        }
    }
}
