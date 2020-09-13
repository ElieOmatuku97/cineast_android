package elieomatuku.cineast_android.vu

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.activity.ItemListActivity
import elieomatuku.cineast_android.adapter.MovieListAdapter
import elieomatuku.cineast_android.core.model.Movie
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_movie.view.*


class SimilarMovieVu(inflater: LayoutInflater,
                     activity: Activity,
                     fragmentWrapper: FragmentWrapper?,
                     parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    override fun getRootViewLayoutId(): Int {
        return R.layout.holder_movie
    }

    private val itemSelectPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    val itemSelectObservable: Observable<Movie>
        get() = itemSelectPublisher.hide()


    private val listView: RecyclerView by lazy {
        rootView.recyclerview_popular_movie
    }

    private val sectionTitleView: TextView by lazy {
        rootView.section_title
    }

    private val seeAllClickView: LinearLayout by lazy {
        rootView.see_all
    }

    private val adapter: MovieListAdapter by lazy {
        MovieListAdapter(itemSelectPublisher)
    }

    override fun onCreate() {
        super.onCreate()
        sectionTitleView.text = activity.getText(R.string.movies)
    }


    fun updateVu(similarMovies: List<Movie>) {
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapter.movies = similarMovies.toMutableList()
        adapter.notifyDataSetChanged()

        seeAllClickView.setOnClickListener {
            ItemListActivity.startItemListActivity(activity, similarMovies, R.string.movies)
        }
    }
}