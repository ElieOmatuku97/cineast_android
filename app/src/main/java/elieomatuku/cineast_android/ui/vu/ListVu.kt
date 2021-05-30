package elieomatuku.cineast_android.ui.vu

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.core.model.Movie
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_item_list.view.*

abstract class ListVu(
    inflater: LayoutInflater,
    activity: Activity,
    fragmentWrapper: FragmentWrapper?,
    parentView: ViewGroup?
) : ToolbarVu(inflater, activity = activity, fragmentWrapper = fragmentWrapper, parentView = parentView) {

    companion object {
        const val FIRST_WIDGET_TYPE_OCCURENCE = 0
    }

    abstract val adapter: RecyclerView.Adapter<*>

    override val toolbar: Toolbar?
        get() = rootView.toolbar

    override fun getRootViewLayoutId(): Int {
        return R.layout.activity_item_list
    }

    protected val listView: RecyclerView by lazy {
        rootView.list_view_container
    }

    protected val movieSelectPublisher: PublishSubject<Content> by lazy {
        PublishSubject.create<Content>()
    }

    val movieSelectObservable: Observable<Content>
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

    fun updateVu(contents: List<Content>?, screenNameRes: Int? = null) {
        setToolbarTitle(screenNameRes)
        contents?.let {
            if (it.isNotEmpty()) {
                setUpListView(it)
            }
        }
    }

    private fun setToolbarTitle(screenNameRes: Int? = null) {
        toolbar?.title = screenNameRes?.let {
            activity.resources.getString(it)
        } ?: activity.resources.getString(R.string.nav_title_discover)
    }

    abstract fun setUpListView(contents: List<Content>)
}
