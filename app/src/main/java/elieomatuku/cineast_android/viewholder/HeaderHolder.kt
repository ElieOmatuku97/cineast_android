package elieomatuku.cineast_android.viewholder


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.HeaderAdapter
import elieomatuku.cineast_android.business.model.data.Movie
import kotlinx.android.synthetic.main.holder_header.view.*
import io.reactivex.subjects.PublishSubject
import timber.log.Timber


class HeaderHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View{
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_header, parent, false)
        }

        fun newInstance(parent: ViewGroup): HeaderHolder{
            return HeaderHolder(createView(parent))
        }
    }
    private val listView: RecyclerView by lazy {
          itemView.header_popular_movie
    }

    private val smoothScroller: RecyclerView.SmoothScroller by lazy {
        object : LinearSmoothScroller(itemView.context) {
            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }
        }
    }

    fun update(movies: List<Movie>, onItemClickPublisher: PublishSubject<Movie>, currentMovieId: Int? = null){
        if (movies != null) {
            val adapter = HeaderAdapter(movies, onItemClickPublisher)
            listView.adapter = adapter
            adapter.notifyDataSetChanged()
            listView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            listView.visibility = View.VISIBLE

            if (currentMovieId != null) {
                Log.d (HeaderHolder::class.java.simpleName, "current movie: $currentMovieId")
                val lm = listView.layoutManager
                val position = adapter.getArticlePosition(currentMovieId)

                if ((position >= 0) && (position < adapter.itemCount)) {
//                    scrollToPositionInList(position, true)
                    smoothScroller.setTargetPosition(position);
                    listView.post {
                        lm?.startSmoothScroll(smoothScroller);
                    }

                } else {
                    Timber.w("focusOnArticle fail. No such article with position $position mathcing key in list")
                }

            }

        } else {
            listView.visibility = View.GONE
        }
    }
}