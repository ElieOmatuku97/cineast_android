package elieomatuku.restapipractice.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.restapipractice.business.business.model.data.PeopleCast
import elieomatuku.restapipractice.viewholder.itemHolder.MovieItemHolder
import io.reactivex.subjects.PublishSubject

class PeopleCastAdapter(private val peopleCast: List<PeopleCast> ,  private val onItemClickPublisher: PublishSubject<Int> ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return peopleCast.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MovieItemHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemMovieHolder = holder as MovieItemHolder
        val movie = peopleCast[position]
        itemMovieHolder.update(movie.poster_path, movie.release_date, movie.original_title)

        itemMovieHolder.itemView.setOnClickListener {
            if (movie.id != null) {
                onItemClickPublisher.onNext(movie.id)
            }
        }
    }
}