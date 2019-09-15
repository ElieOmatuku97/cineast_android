package elieomatuku.cineast_android.adapter

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import elieomatuku.cineast_android.fragment.OverviewFragment
import elieomatuku.cineast_android.fragment.MovieTeamFragment
import elieomatuku.cineast_android.fragment.SimilarMovieFragment
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.model.data.*
import elieomatuku.cineast_android.viewholder.itemHolder.*
import io.reactivex.subjects.PublishSubject

class MovieItemAdapter(private val movieSummary: MovieSummary,
                       private val onProfileClickedPicturePublisher: PublishSubject<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_MOVIE_PROFILE = 0
        const val TYPE_MENU_MOVIE = 1
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            TYPE_MOVIE_PROFILE -> TYPE_MOVIE_PROFILE
            TYPE_MENU_MOVIE -> TYPE_MENU_MOVIE
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MOVIE_PROFILE -> ProfileMovieHolder.newInstance(parent, onProfileClickedPicturePublisher)
            TYPE_MENU_MOVIE -> MenuMovieHolder.newInstance(parent)
            else -> throw RuntimeException("View Type does not exist.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            TYPE_MOVIE_PROFILE -> {
                val movieProfileHolder = holder as ProfileMovieHolder
                movieProfileHolder.update(movieSummary)
            }

            TYPE_MENU_MOVIE -> {
                val menuMovieHolder = holder as MenuMovieHolder
                menuMovieHolder.overviewSegmentBtn.setOnClickListener {
                    val activity = it.context as FragmentActivity
                    val overviewFragment = OverviewFragment.newInstance(movieSummary)
                    (activity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, overviewFragment).commit()

                }

                menuMovieHolder.peopleSegmentBtn.setOnClickListener {
                    val activity = it.context as FragmentActivity
                    val peopleFragment = MovieTeamFragment.newInstance(movieSummary)
                    (activity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, peopleFragment).commit()
                }

                menuMovieHolder.similarSegmentBtn.setOnClickListener {
                    val activity = it.context as FragmentActivity
                    val similarFragment = SimilarMovieFragment.newInstance(movieSummary)
                    (activity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, similarFragment).commit()
                }
            }

        }
    }
}
