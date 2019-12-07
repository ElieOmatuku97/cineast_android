package elieomatuku.cineast_android.vu

import android.app.Activity
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.MovieListAdapter
import elieomatuku.cineast_android.model.data.Movie
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.view.*


class PopularMoviesVu(inflater: LayoutInflater,
                      activity: Activity,
                      fragmentWrapper: FragmentWrapper?,
                      parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    override fun getRootViewLayoutId() = R.layout.fragment_search


    private val movieSelectPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    val movieSelectObservable: Observable<Movie>
        get() = movieSelectPublisher.hide()

    private val gridView by lazy {
        rootView.grid_view
    }

    private val GRIDVIEW_NUMBER_OF_COLUMNS = 2

    var gridLayoutManager: GridLayoutManager? = null
    private val adapter: MovieListAdapter by lazy {
        MovieListAdapter(movieSelectPublisher, R.layout.holder_popular_movie)
    }

    override fun onCreate() {
        super.onCreate()
        gridLayoutManager = GridLayoutManager(this.fragmentWrapper?.support?.context, GRIDVIEW_NUMBER_OF_COLUMNS)
        gridView.adapter = adapter
    }

    fun populateGridView(movies: List<Movie>) {
        adapter.movies = movies.toMutableList()
        gridView.layoutManager = gridLayoutManager
        adapter.notifyDataSetChanged()
    }

    fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        gridView.layoutManager = LinearLayoutManager(activity)
        adapter.notifyDataSetChanged()
    }
}