package elieomatuku.cineast_android.vu

import android.app.Activity
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.MovieListAdapter
import elieomatuku.cineast_android.business.model.data.Movie
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.view.*


class PopularMoviesVu (inflater: LayoutInflater,
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
    var movieListAdapter : MovieListAdapter? =  null

    override fun onCreate() {
        super.onCreate()

        gridLayoutManager = GridLayoutManager(this.fragmentWrapper?.support?.context, GRIDVIEW_NUMBER_OF_COLUMNS)
        gridView.layoutManager = gridLayoutManager

    }

    fun populateGridView(movies: List<Movie>) {
        movieListAdapter =  MovieListAdapter(movies, movieSelectPublisher, R.layout.holder_popular_movie)
        gridView.adapter = movieListAdapter
        movieListAdapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        gridView.adapter = null
        gridView.layoutManager = null
        gridLayoutManager = null
        movieListAdapter = null
    }
}