package elieomatuku.cineast_android.vu

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.MovieAdapter
import elieomatuku.cineast_android.adapter.PopularPeopleItemAdapter
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.model.data.Person
import elieomatuku.cineast_android.business.model.data.Widget
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.vu_item_list.view.*


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


    fun updateVu(widgets: List<Widget>?, screenNameRes: Int? = null) {
        if (screenNameRes != null) {
            toolbar?.title = activity.resources.getString(screenNameRes)
        } else {
            toolbar?.title = activity.resources.getString(R.string.nav_title_discover)
        }

        if (widgets != null && widgets.isNotEmpty()) {
            if (areWidgetsMovies(widgets)) {
                listView.adapter = MovieAdapter(widgets as List<Movie>, movieSelectPublisher, R.layout.holder_movie_list)
                listView.layoutManager = LinearLayoutManager(activity)
            } else {
                listView.adapter = PopularPeopleItemAdapter(widgets as List<Person>, personSelectPublisher, R.layout.holder_people_list)
                listView.layoutManager = LinearLayoutManager(activity)
            }
        }
    }

    private fun areWidgetsMovies(widgets: List<Widget?>) : Boolean {
        return (widgets[FIRST_WIDGET_TYPE_OCCURENCE] != null) && (widgets[FIRST_WIDGET_TYPE_OCCURENCE] is Movie)
    }
}