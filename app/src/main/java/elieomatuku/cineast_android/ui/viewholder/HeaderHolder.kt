package elieomatuku.cineast_android.ui.viewholder


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.ui.adapter.HeaderAdapter
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.core.model.Movie
import kotlinx.android.synthetic.main.holder_header.view.*
import io.reactivex.subjects.PublishSubject


class HeaderHolder(itemView: View, private val onItemClickPublisher: PublishSubject<Movie>) : ContentHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_header, parent, false)
        }

        fun newInstance(parent: ViewGroup, onItemClickPublisher: PublishSubject<Movie>): HeaderHolder {
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


    override fun update(content: Pair<Int, List<Content>>) {
        val movies = content.second as List<Movie>

        val adapter = HeaderAdapter(movies, onItemClickPublisher)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
        listView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        listView.visibility = View.VISIBLE

        val lm = listView.layoutManager
        val position = adapter.getArticlePosition(CURRENT_MOVIE_ID)

        if ((position >= 0) && (position < adapter.itemCount)) {
//            scrollToPositionInList(position, true)
            smoothScroller.setTargetPosition(position)
            listView.post {
                lm?.startSmoothScroll(smoothScroller)
            }
        }
    }
}