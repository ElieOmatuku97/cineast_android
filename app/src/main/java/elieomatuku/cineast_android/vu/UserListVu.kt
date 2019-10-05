package elieomatuku.cineast_android.vu

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.UserMovieListAdapter
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.model.data.Widget
import elieomatuku.cineast_android.callback.SwipeToDeleteCallback
import io.chthonic.mythos.mvp.FragmentWrapper


class UserListVu(inflater: LayoutInflater, activity: Activity, fragmentWrapper: FragmentWrapper?,
                 parentView: ViewGroup?) : ListVu(inflater, activity = activity, fragmentWrapper = fragmentWrapper, parentView = parentView) {


    override fun setUpListView(widgets: List<Widget>) {
        adapter = UserMovieListAdapter(widgets as List<Movie>, movieSelectPublisher, R.layout.holder_movie_list, onMovieRemovedPublisher)
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter as UserMovieListAdapter))
        itemTouchHelper.attachToRecyclerView(listView)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

}