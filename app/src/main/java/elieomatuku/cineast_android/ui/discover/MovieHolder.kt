package elieomatuku.cineast_android.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentContainerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.ui.details.MoviesFragment
import elieomatuku.cineast_android.ui.viewholder.ContentHolder
import kotlinx.android.synthetic.main.holder_movie.view.*

class MovieHolder(itemView: View) : ContentHolder(itemView) {
    companion object {
        fun createView(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context).inflate(R.layout.holder_movie, parent, false)
        }

        fun newInstance(parent: ViewGroup): MovieHolder {
            return MovieHolder(createView(parent))
        }
    }

    private val rootLayout: ConstraintLayout by lazy {
        itemView.root_layout
    }

    private val fragmentContainerView: FragmentContainerView by lazy {
        val fragmentContainerView = FragmentContainerView(itemView.context)
        fragmentContainerView.id = View.generateViewId()
        fragmentContainerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        fragmentContainerView
    }

    override fun update(content: Pair<Int, List<Content>>) {
        val movies = content.second as List<Movie>
        val titleRes = content.first

        val fm = (itemView.context as AppCompatActivity).supportFragmentManager
        val fragment = MoviesFragment.newInstance(movies, itemView.context.getString(titleRes))
        fm.beginTransaction().replace(fragmentContainerView.id, fragment).addToBackStack(null).commit()

        if (fragmentContainerView.parent == null) {
            rootLayout.addView(fragmentContainerView, 0)
            val set = ConstraintSet()
            set.clone(rootLayout)
            set.applyTo(rootLayout)
        }
    }
}
