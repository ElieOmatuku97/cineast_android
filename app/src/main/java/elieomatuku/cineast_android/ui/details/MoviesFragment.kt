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
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.core.model.Genre
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.databinding.FragmentMoviesBinding
import elieomatuku.cineast_android.ui.contents.ContentsActivity
import elieomatuku.cineast_android.ui.contents.ContentsAdapter
import elieomatuku.cineast_android.ui.details.movie.MovieActivity
import elieomatuku.cineast_android.ui.base.BaseFragment
import elieomatuku.cineast_android.utils.Constants
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
        private const val SELECTED_MOVIE_TITLE = "gotomovie_title"
        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"

        fun newInstance(movies: List<Movie>, title: String? = null, selectedMovieTitle: String? = null): MoviesFragment {
            val args = Bundle()
            args.putParcelableArrayList(MOVIES, movies as ArrayList<out Parcelable>)

            title?.let {
                args.putString(TITLE, it)
            }

            selectedMovieTitle?.let {
                args.putString(SELECTED_MOVIE_TITLE, it)
            }

            val fragment = MoviesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewDataBinding: FragmentMoviesBinding

    private val viewModel: MoviesViewModel by viewModels()

    private val movieSelectPublisher: PublishSubject<Content> by lazy {
        PublishSubject.create<Content>()
    }

    private val movieSelectObservable: Observable<Content>
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

    private val adapter: ContentsAdapter by lazy {
        ContentsAdapter(movieSelectPublisher)
    }

    private lateinit var movies: List<Movie>
    private var title: String? = null
    private var selectedMovieTitle: String? = null
    private var genres: List<Genre>? = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_movies, container, false)
        viewDataBinding = FragmentMoviesBinding.bind(rootView)

        arguments?.getParcelableArrayList<Movie>(MOVIES)?.let {
            movies = it
        }

        viewModel.genresLiveData.observe(
            this.viewLifecycleOwner,
            Observer {
                genres = it
            }
        )

        updateView()

        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()

        rxSubs.add(
            (
                movieSelectObservable
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { movie: Content ->
                            val params = Bundle()
                            params.putString(Constants.SCREEN_NAME_KEY, selectedMovieTitle)
                            params.putParcelable(MOVIE_KEY, movie)
                            params.putParcelableArrayList(MOVIE_GENRES_KEY, genres as ArrayList<out Parcelable>)
                            gotoMovie(params)
                        },
                        { t: Throwable ->
                            Timber.e("movieSelectObservable failed:$t")
                        }
                    )
                )
        )
    }

    private fun updateView() {
        setTitle(arguments?.getString(TITLE))
        setSelectedMovieTitle(arguments?.getString(SELECTED_MOVIE_TITLE))
        sectionTitleView.text = title
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapter.contents = movies.toMutableList()
        adapter.notifyDataSetChanged()

        seeAllClickView.setOnClickListener {
            context?.let {
                ContentsActivity.startActivity(it, movies, R.string.movies)
            }
        }
    }

    private fun setTitle(title: String?) {
        if (title != null) {
            this.title = title
        } else {
            this.title = getString(R.string.movies)
        }
    }

    private fun setSelectedMovieTitle(title: String?) {
        if (title != null) {
            this.selectedMovieTitle = title
        } else {
            this.selectedMovieTitle = this.title
        }
    }

    private fun gotoMovie(params: Bundle) {
        val intent = Intent(activity, MovieActivity::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}
