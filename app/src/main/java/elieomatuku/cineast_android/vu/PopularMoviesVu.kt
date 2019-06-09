package elieomatuku.restapipractice.vu

import android.app.Activity
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.restapipractice.R
import elieomatuku.restapipractice.adapter.MovieAdapter
import elieomatuku.restapipractice.business.business.model.data.Movie
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

    var gridLayoutManager: GridLayoutManager ? = null
    var movieAdapter : MovieAdapter? =  null

    override fun onCreate() {
        super.onCreate()

        gridLayoutManager = GridLayoutManager(this.fragmentWrapper?.support?.context, GRIDVIEW_NUMBER_OF_COLUMNS)
        gridView.layoutManager = gridLayoutManager

    }

    fun populateGridView(movies: List<Movie>) {
        movieAdapter =  MovieAdapter(movies, movieSelectPublisher, R.layout.holder_popular_movie)
        gridView.adapter = movieAdapter
        movieAdapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        gridView.adapter = null
        gridView.layoutManager = null
        gridLayoutManager = null
        movieAdapter = null
    }
}