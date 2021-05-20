package elieomatuku.cineast_android.ui.details

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.ui.adapter.MoviesAdapter
import elieomatuku.cineast_android.ui.vu.BaseVu
import elieomatuku.cineast_android.ui.content_list.ContentListActivity
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_movies.view.*

/**
 * Created by elieomatuku on 2020-09-13
 */

class MoviesVu(
    inflater: LayoutInflater,
    activity: Activity,
    fragmentWrapper: FragmentWrapper?,
    parentView: ViewGroup?
) : BaseVu(
    inflater,
    activity = activity,
    fragmentWrapper = fragmentWrapper,
    parentView = parentView
) {

    override fun getRootViewLayoutId(): Int {
        return R.layout.fragment_movies
    }

    private val movieSelectPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    val movieSelectObservable: Observable<Movie>
        get() = movieSelectPublisher.hide()

    private val listView: RecyclerView by lazy {
        rootView.recyclerview_movie
    }

    private val sectionTitleView: TextView by lazy {
        rootView.section_title
    }

    private val seeAllClickView: LinearLayout by lazy {
        rootView.see_all
    }

    private val adapter: MoviesAdapter by lazy {
        MoviesAdapter(movieSelectPublisher)
    }

    fun updateVu(movies: List<Movie>, titleRes: Int = R.string.movies) {
        sectionTitleView.text = activity.getText(titleRes)
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapter.movies = movies.toMutableList()
        adapter.notifyDataSetChanged()

        seeAllClickView.setOnClickListener {
            ContentListActivity.startItemListActivity(activity, movies, R.string.movies)
        }
    }
}
