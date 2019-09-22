package elieomatuku.cineast_android.vu

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.activity.ItemListActivity
import elieomatuku.cineast_android.adapter.MovieListAdapter
import elieomatuku.cineast_android.adapter.PopularPeopleItemAdapter
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.model.data.Person
import elieomatuku.cineast_android.business.model.data.Widget
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.vu_item_list.view.*
import elieomatuku.cineast_android.callback.SwipeToDeleteCallback
import androidx.recyclerview.widget.ItemTouchHelper
import elieomatuku.cineast_android.adapter.UserMovieListAdapter


class ItemListVu (inflater: LayoutInflater,
                  activity: Activity,
                  fragmentWrapper: FragmentWrapper?,
                  parentView: ViewGroup?) : ToolbarVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    companion object {
        const val FIRST_WIDGET_TYPE_OCCURENCE = 0
    }

    override val toolbar: Toolbar?
        get() = rootView.toolbar

    override fun getRootViewLayoutId(): Int {
        return R.layout.vu_item_list
    }

    private val listView: RecyclerView by lazy {
        rootView.list_view_container
    }

    private val movieSelectPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    val movieSelectObservable: Observable<Movie>
        get() = movieSelectPublisher.hide()


    private val personSelectPublisher: PublishSubject<Person> by lazy {
        PublishSubject.create<Person>()
    }

    val personSelectObservable: Observable<Person>
        get() = personSelectPublisher.hide()

    val userListCheckPublisher: PublishSubject<Boolean> ? by lazy {
        if (activity is ItemListActivity) {
            activity.userListCheckPublisher
        } else {
            null
        }
    }

    private val onMovieRemovedPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    val onMovieRemovedObservable: Observable<Movie>
        get() = onMovieRemovedPublisher.hide()

    fun updateVu(widgets: List<Widget>?, screenNameRes: Int? = null, isUserList: Boolean = false) {
        setToolbarTitle(screenNameRes)
        widgets?.let {
            if(!it.isEmpty()) {
                setUpListView(it, isUserList)
            }
        }
    }

    private fun setToolbarTitle(screenNameRes: Int? = null) {
        toolbar?.title =  screenNameRes?.let {
            activity.resources.getString(it) } ?: activity.resources.getString(R.string.nav_title_discover)

    }

    private fun setUpListView(widgets: List<Widget>, isUserList: Boolean) {
        if (areWidgetsMovies(widgets)) {
            val movieListAdapter =  getMovieAdapter(widgets, isUserList)
            setSwipeToDelete(movieListAdapter)
            listView.adapter = movieListAdapter

        } else {
            listView.adapter = PopularPeopleItemAdapter(widgets as List<Person>, personSelectPublisher, R.layout.holder_people_list)
        }

        listView.layoutManager = LinearLayoutManager(activity)
    }


    private fun areWidgetsMovies(widgets: List<Widget?>) : Boolean {
        return (widgets[FIRST_WIDGET_TYPE_OCCURENCE] != null) && (widgets[FIRST_WIDGET_TYPE_OCCURENCE] is Movie)
    }

    private fun getMovieAdapter(widgets: List<Widget>, isUserList: Boolean) : MovieListAdapter {
        return if (isUserList){
            UserMovieListAdapter(widgets as List<Movie>, movieSelectPublisher, R.layout.holder_movie_list, onMovieRemovedPublisher)
        } else {
            MovieListAdapter(widgets as List<Movie>, movieSelectPublisher, R.layout.holder_movie_list)
        }
    }

    private fun setSwipeToDelete(movieListAdapter: MovieListAdapter) {
        if (movieListAdapter is UserMovieListAdapter) {
            val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(movieListAdapter))
            itemTouchHelper.attachToRecyclerView(listView)
        }
    }
}