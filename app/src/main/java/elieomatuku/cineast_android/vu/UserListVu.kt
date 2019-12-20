package elieomatuku.cineast_android.vu

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.MovieListAdapter
import elieomatuku.cineast_android.adapter.UserMovieListAdapter
import elieomatuku.cineast_android.model.data.Movie

import elieomatuku.cineast_android.model.data.Content
import elieomatuku.cineast_android.callback.SwipeToDeleteCallback
import io.chthonic.mythos.mvp.FragmentWrapper


class UserListVu(inflater: LayoutInflater, activity: Activity, fragmentWrapper: FragmentWrapper?,
                 parentView: ViewGroup?) : ListVu(inflater, activity = activity, fragmentWrapper = fragmentWrapper, parentView = parentView) {


    override val adapter: MovieListAdapter by lazy {
        UserMovieListAdapter(movieSelectPublisher, R.layout.holder_movie_list, onMovieRemovedPublisher)
    }



    override fun setUpListView(contents: List<Content>) {
        adapter.movies = contents as MutableList<Movie>
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter as UserMovieListAdapter))
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