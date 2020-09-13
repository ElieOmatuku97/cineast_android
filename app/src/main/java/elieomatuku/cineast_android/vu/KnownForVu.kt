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
import elieomatuku.cineast_android.adapter.MoviesAdapter
import elieomatuku.cineast_android.core.model.KnownFor
import elieomatuku.cineast_android.core.model.Movie
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.holder_movie.view.*

class KnownForVu (inflater: LayoutInflater,
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


    private val listView : RecyclerView by lazy {
        rootView.recyclerview_popular_movie
    }

    private val sectionTitleView: TextView by lazy {
        rootView.section_title
    }

    private val adapter: MoviesAdapter by lazy {
        MoviesAdapter(itemSelectPublisher)
    }

    private val seeAllClickView: LinearLayout by lazy {
        rootView.see_all
    }

    override fun onCreate() {
        super.onCreate()
        sectionTitleView.text = activity.getText(R.string.cast)
    }


    fun updateVu(knownFor: List<KnownFor>){
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val movies = knownFor.map {it.toMovie()}.filterNotNull()
        adapter.movies = movies.toMutableList()
        adapter.notifyDataSetChanged()

        seeAllClickView.setOnClickListener {
            ItemListActivity.startItemListActivity(activity, movies, R.string.movies)
        }
    }

}