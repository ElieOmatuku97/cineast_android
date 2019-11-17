package elieomatuku.cineast_android.vu

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.MovieListAdapter
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.model.data.Widget
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.vu_item_list.view.*



abstract class ListVu(inflater: LayoutInflater,
             activity: Activity,
             fragmentWrapper: FragmentWrapper?,
             parentView: ViewGroup?) : ToolbarVu(inflater, activity = activity, fragmentWrapper = fragmentWrapper, parentView = parentView) {

    companion object {
        const val FIRST_WIDGET_TYPE_OCCURENCE = 0
    }

    abstract val adapter: RecyclerView.Adapter<*>

    override val toolbar: Toolbar?
        get() = rootView.toolbar

    override fun getRootViewLayoutId(): Int {
        return R.layout.vu_item_list
    }

    protected val listView: RecyclerView by lazy {
        rootView.list_view_container
    }

    protected val movieSelectPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    val movieSelectObservable: Observable<Movie>
        get() = movieSelectPublisher.hide()


    override fun onCreate() {
        super.onCreate()
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity)
    }

    protected val onMovieRemovedPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    val onMovieRemovedObservable: Observable<Movie>
        get() = onMovieRemovedPublisher.hide()

    fun updateVu(widgets: List<Widget>?, screenNameRes: Int? = null) {
        setToolbarTitle(screenNameRes)
        widgets?.let {
            if (!it.isEmpty()) {
                setUpListView(it)
            }
        }
    }

    private fun setToolbarTitle(screenNameRes: Int? = null) {
        toolbar?.title = screenNameRes?.let {
            activity.resources.getString(it)
        } ?: activity.resources.getString(R.string.nav_title_discover)

    }

    abstract fun setUpListView(widgets: List<Widget>)
}