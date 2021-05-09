package elieomatuku.cineast_android.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.ui.common_viewholder.ContentHolder
import elieomatuku.cineast_android.ui.details.MoviesFragment
import kotlinx.android.synthetic.main.holder_movie.view.*
import timber.log.Timber

class MovieHolder(itemView: View) : ContentHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_movie, parent, false)
        }

        fun newInstance(parent: ViewGroup): MovieHolder {
            return MovieHolder(createView(parent))
        }
    }

    private val rootLayout: FrameLayout by lazy {
        val view = itemView.layout_root
        view
    }

    override fun update(content: Pair<Int, List<Content>>) {
        val movies = content.second as List<Movie>
        val titleRes = content.first

        Timber.d("movies from discover: $movies")

        rootLayout.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val fm = (itemView.context as AppCompatActivity).supportFragmentManager
        val fragment = MoviesFragment.newInstance(movies, itemView.context.getString(titleRes))

        fm.beginTransaction().add(android.R.id.content, fragment, MoviesFragment.TAG).addToBackStack(null).commit()
    }
}