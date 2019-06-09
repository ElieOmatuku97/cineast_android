package elieomatuku.cineast_android.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.business.model.data.*
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.viewholder.*
import elieomatuku.cineast_android.viewholder.itemHolder.LoginViewHolder
import io.reactivex.subjects.PublishSubject

class DiscoverAdapter(private val onMovieClickPublisher: PublishSubject<Movie>, private val onPersonClickPublisher: PublishSubject<Person>,
                      private val loginClickPublisher: PublishSubject<Boolean>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object {
        const val POSITION_HEADER = 0
        const val TYPE_HEADER = 0
        const val TYPE_POPULAR_MOVIE = 1
        const val TYPE_POPULAR_PEOPLE = 2
        const val TYPE_NOW_PLAYING_MOVIE = 3
        const val TYPE_UPCOMING_MOVIE = 4
        const val TYPE_TOP_RATED_MOVIE = 5
        const val TYPE_LOGIN = 6
    }

    var widget : List<Widget> = listOf()
    var widgetMap: Map<String, List<Widget>> = mapOf()

    private var filteredWidget : MutableMap<Int, List<Widget>?> = mutableMapOf()

    private fun getDiscoverPosition(position: Int): Int {
        return position - 1
    }

    var currentMovieId: Int =  -1

    override fun getItemCount(): Int {
        filteredWidget = UiUtils.filterWidgets(widgetMap)

        return if (filteredWidget != null) {
            filteredWidget.size + 2
        } else {
            0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position){
            POSITION_HEADER -> TYPE_HEADER
            TYPE_POPULAR_MOVIE -> TYPE_POPULAR_MOVIE
            TYPE_POPULAR_PEOPLE -> TYPE_POPULAR_PEOPLE
            TYPE_NOW_PLAYING_MOVIE -> TYPE_NOW_PLAYING_MOVIE
            TYPE_UPCOMING_MOVIE -> TYPE_UPCOMING_MOVIE
            TYPE_TOP_RATED_MOVIE -> TYPE_TOP_RATED_MOVIE
            TYPE_LOGIN -> TYPE_LOGIN
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  when (viewType){
            TYPE_HEADER -> {
                HeaderHolder.newInstance(parent)
            }
            TYPE_POPULAR_MOVIE -> {
                MovieHolder.newInstance(parent)
            }
            TYPE_POPULAR_PEOPLE ->{
                PopularPeopleHolder.newInstance(parent)
            }
            TYPE_NOW_PLAYING_MOVIE -> {
                MovieHolder.newInstance(parent)
            }
            TYPE_UPCOMING_MOVIE -> {
                MovieHolder.newInstance(parent)
            }
            TYPE_TOP_RATED_MOVIE -> {
                MovieHolder.newInstance(parent)
            }
            TYPE_LOGIN -> {
                LoginViewHolder.newInstance(parent)
            }
            else -> throw RuntimeException("View Type does not exist.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType){
            TYPE_HEADER -> {
                 val movies =  filteredWidget[TYPE_HEADER] as List<Movie>
                 val headerHolder = holder as HeaderHolder
                 headerHolder.update(movies, onMovieClickPublisher, currentMovieId)
            }
            TYPE_POPULAR_MOVIE -> {
                val movies =  filteredWidget[getDiscoverPosition(TYPE_POPULAR_MOVIE)] as List<Movie>
                (holder as MovieHolder).update(movies , R.string.popular_movies, onMovieClickPublisher)
            }
            TYPE_POPULAR_PEOPLE ->{
                val popularPeople = filteredWidget[getDiscoverPosition(TYPE_POPULAR_PEOPLE)] as List<People>
                (holder as PopularPeopleHolder).update(popularPeople, onPersonClickPublisher)
            }
            TYPE_NOW_PLAYING_MOVIE -> {
                val nowPlayingMovies = filteredWidget[getDiscoverPosition(TYPE_NOW_PLAYING_MOVIE)] as List<Movie>
                (holder as  MovieHolder).update(nowPlayingMovies, R.string.now_playing, onMovieClickPublisher)
            }
            TYPE_UPCOMING_MOVIE -> {
                val upcomingMovies = filteredWidget[getDiscoverPosition(TYPE_UPCOMING_MOVIE)] as List<Movie>
                (holder as  MovieHolder).update(upcomingMovies, R.string.upcoming, onMovieClickPublisher)
            }
            TYPE_TOP_RATED_MOVIE -> {
                val topRatedMovies =  filteredWidget[getDiscoverPosition(TYPE_TOP_RATED_MOVIE)] as List<Movie>
                (holder as  MovieHolder).update(topRatedMovies, R.string.top_rated, onMovieClickPublisher)
            }

            TYPE_LOGIN -> {
                (holder as LoginViewHolder).itemView.setOnClickListener {
                    loginClickPublisher.onNext(true)
                }
            }
        }
    }
}