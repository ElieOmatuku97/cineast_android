package elieomatuku.cineast_android.details

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.content_list.ContentListActivity
import elieomatuku.cineast_android.adapter.MoviesAdapter
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.vu.BaseVu
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_movie.view.*


/**
 * Created by elieomatuku on 2020-09-13
 */

class MoviesVu(inflater: LayoutInflater,
                     activity: Activity,
                     fragmentWrapper: FragmentWrapper?,
                     parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    override fun getRootViewLayoutId(): Int {
        return R.layout.holder_movie
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