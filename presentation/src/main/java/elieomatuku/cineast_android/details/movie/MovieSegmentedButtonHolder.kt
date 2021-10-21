package elieomatuku.cineast_android.details.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.MovieSummary
import kotlinx.android.synthetic.main.holder_menu_movie.view.*

class MovieSegmentedButtonHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_menu_movie, parent, false)
        }

        fun newInstance(parent: ViewGroup): MovieSegmentedButtonHolder {
            return MovieSegmentedButtonHolder(createView(parent))
        }
    }

    val overviewSegmentBtn: AppCompatRadioButton by lazy {
        itemView.overview
    }

    val similarSegmentBtn: AppCompatRadioButton by lazy {
        itemView.similar
    }

    val peopleSegmentBtn: AppCompatRadioButton by lazy {
        itemView.people
    }

    fun update(movieSummary: MovieSummary, checkedTab: String) {
        itemView.visibility = if (movieSummary.movie != null) View.VISIBLE else View.GONE

        when (checkedTab) {
            MovieActivity.MOVIE_OVERVIEW -> overviewSegmentBtn.isChecked = true
            MovieActivity.MOVIE_CREW -> peopleSegmentBtn.isChecked = true
            MovieActivity.SIMILAR_MOVIES -> similarSegmentBtn.isChecked = true
        }
    }
}
