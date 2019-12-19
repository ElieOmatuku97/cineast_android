package elieomatuku.cineast_android.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.activity.ItemListActivity
import elieomatuku.cineast_android.adapter.MovieListAdapter
import elieomatuku.cineast_android.model.data.Movie
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_movie.view.*
import timber.log.Timber

class MovieHolder(itemView: View, private val onItemClickPublisher: PublishSubject<Movie>): RecyclerView.ViewHolder (itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_movie, parent, false)
        }

        fun newInstance(parent: ViewGroup, onItemClickPublisher: PublishSubject<Movie>): MovieHolder{
            return MovieHolder(createView(parent), onItemClickPublisher)
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

    private val adapter :  MovieListAdapter by lazy {
        MovieListAdapter(onItemClickPublisher)
    }

    fun update(movies: List<Movie>, resources: Int){

        Timber.d("movies from discover: $movies")

        if (movies.isNotEmpty()) {
            sectionTitle.text = itemView.context.getString(resources)
            adapter.movies = movies.toMutableList()
            listView.adapter = adapter
            listView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            listView.visibility = View.VISIBLE
        }

        seeAllView.setOnClickListener {
            ItemListActivity.startItemListActivity(itemView.context, movies, resources)
        }
    }
}