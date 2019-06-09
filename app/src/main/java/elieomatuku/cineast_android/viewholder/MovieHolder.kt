package elieomatuku.restapipractice.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.restapipractice.R
import elieomatuku.restapipractice.adapter.MovieAdapter
import elieomatuku.restapipractice.business.business.model.data.Movie
import elieomatuku.restapipractice.utils.UiUtils
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_movie.view.*

class MovieHolder(itemView: View): RecyclerView.ViewHolder (itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_movie, parent, false)
        }

        fun newInstance(parent: ViewGroup): MovieHolder{
            return MovieHolder(createView(parent))
        }
    }

    private val sectionTitle by lazy {
        itemView.section_title
    }

    private val seeAllView by lazy {
        itemView.see_all
    }

    private val listView by lazy {
        itemView.recyclerview_popular_movie
    }

    fun update(movies: List<Movie>, resources: Int, onItemClickPublisher: PublishSubject<Movie>){
        sectionTitle.text = itemView.context.getString(resources)
        listView.adapter = MovieAdapter(movies, onItemClickPublisher)
        listView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        seeAllView.setOnClickListener {
            UiUtils.startItemListActivity(itemView.context, movies, resources)
        }
    }
}