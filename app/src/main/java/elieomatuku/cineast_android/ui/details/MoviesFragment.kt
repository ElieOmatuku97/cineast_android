package elieomatuku.cineast_android.ui.details

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Genre
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.databinding.FragmentMoviesBinding
import elieomatuku.cineast_android.ui.common_adapter.MoviesAdapter
import elieomatuku.cineast_android.ui.common_fragment.BaseFragment
import elieomatuku.cineast_android.ui.content_list.ContentListActivity
import elieomatuku.cineast_android.ui.details.movie.MovieActivity
import elieomatuku.cineast_android.ui.details.movie.similar.SimilarMoviePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.ArrayList


/**
 * Created by elieomatuku on 2021-05-05
 */

class MoviesFragment : BaseFragment() {
    companion object {

        const val MOVIES = "movies"
        const val TITLE = "title"
        const val SCREEN_NAME_KEY = "screen_name"
        const val MOVIE_KEY = "movieApi"

        fun newInstance(movies: List<Movie>, title: String): MoviesFragment {
            val args = Bundle()
            args.putParcelableArrayList(MOVIES, movies as ArrayList<out Parcelable>)
            args.putString(TITLE, title)

            val fragment = MoviesFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private lateinit var viewDataBinding: FragmentMoviesBinding

    private val viewModel: MoviesViewModel by viewModels()

    private val movieSelectPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }

    val movieSelectObservable: Observable<Movie>
        get() = movieSelectPublisher.hide()

    private val listView: RecyclerView by lazy {
        viewDataBinding.recyclerviewMovie
    }

    private val sectionTitleView: TextView by lazy {
        viewDataBinding.sectionTitle
    }

    private val seeAllClickView: LinearLayout by lazy {
        viewDataBinding.seeAll
    }

    private val adapter: MoviesAdapter by lazy {
        MoviesAdapter(movieSelectPublisher)
    }

    private lateinit var movies: List<Movie>
    private var title: String? = null
    private var genres: List<Genre>? = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_movies, container, false)
        viewDataBinding = FragmentMoviesBinding.bind(rootView)

        arguments?.getParcelableArrayList<Movie>(MOVIES)?.let {
            movies = it
        }

        arguments?.getString(TITLE)?.let {
            title = it
        }


        viewModel.genresLiveData.observe(this.viewLifecycleOwner, Observer {
            genres = it
        })

        updateView()

        return viewDataBinding.root

    }

    override fun onResume() {
        super.onResume()

        rxSubs.add((movieSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ movie: Movie ->
                    val params = Bundle()
                    params.putString(SCREEN_NAME_KEY, title)
                    params.putParcelable(MOVIE_KEY, movie)
                    params.putParcelableArrayList(SimilarMoviePresenter.MOVIE_GENRES_KEY, genres as ArrayList<out Parcelable>)
                    gotoMovie(params)
                }, { t: Throwable ->
                    Timber.e("movieSelectObservable failed:$t")
                })))
    }


    private fun updateView() {
        sectionTitleView.text = title
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapter.movies = movies.toMutableList()
        adapter.notifyDataSetChanged()

        seeAllClickView.setOnClickListener {
            context?.let {
                ContentListActivity.startItemListActivity(it, movies, R.string.movies)
            }
        }
    }


    private fun gotoMovie(params: Bundle) {
        val intent = Intent(activity, MovieActivity::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}