package elieomatuku.cineast_android.ui.settings.userlist

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.ui.common_adapter.MoviesAdapter
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.ui.common_vu.ListVu
import io.chthonic.mythos.mvp.FragmentWrapper


class UserListVu(inflater: LayoutInflater, activity: Activity, fragmentWrapper: FragmentWrapper?,
                 parentView: ViewGroup?) : ListVu(inflater, activity = activity, fragmentWrapper = fragmentWrapper, parentView = parentView) {


    override val adapter: MoviesAdapter by lazy {
        UserMoviesAdapter(movieSelectPublisher, R.layout.holder_movie_list, onMovieRemovedPublisher)
    }


    override fun setUpListView(contents: List<Content>) {
        adapter.movies = contents as MutableList<Movie>
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter as UserMoviesAdapter))
        itemTouchHelper.attachToRecyclerView(listView)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        listView.layoutManager = LinearLayoutManager(activity)
        adapter.notifyDataSetChanged()
    }

}