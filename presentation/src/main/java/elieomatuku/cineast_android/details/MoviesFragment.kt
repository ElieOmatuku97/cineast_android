package elieomatuku.cineast_android.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.databinding.FragmentMoviesBinding
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.contents.ContentsAdapter
import elieomatuku.cineast_android.details.movie.MovieFragment
import elieomatuku.cineast_android.utils.Constants
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.io.Serializable

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

        fun newInstance(
            movies: List<Movie>,
            title: String? = null,
            selectedMovieTitle: String? = null
        ): MoviesFragment {
            val args = Bundle()
            args.putSerializable(MOVIES, movies as Serializable)

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

    private val viewModel: MoviesViewModel by viewModel<MoviesViewModel>()

    private val movieSelectPublisher: PublishSubject<Content> by lazy {
        PublishSubject.create<Content>()
    }

    private val movieSelectObservable: Observable<Content>
        get() = movieSelectPublisher.hide()

//    private val listView: RecyclerView by lazy {
//        viewDataBinding.recyclerviewMovie
//    }
//
//    private val sectionTitleView: TextView by lazy {
//        viewDataBinding.sectionTitle
//    }
//
//    private val seeAllClickView: LinearLayout by lazy {
//        viewDataBinding.seeAll
//    }

    private val adapter: ContentsAdapter by lazy {
        ContentsAdapter(movieSelectPublisher)
    }

    private lateinit var movies: List<Movie>
    private var title: String? = null
    private var selectedMovieTitle: String? = null
    private var genres: List<Genre>? = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_movies, container, false)
        viewDataBinding = FragmentMoviesBinding.bind(rootView)

        arguments?.getSerializable(MOVIES)?.let {
            movies = it as List<Movie>
        }

        viewModel.viewState.observe(
            this.viewLifecycleOwner
        ) { state ->
            state.genres.let {
                genres = it
            }
        }

        updateView()

        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()
        rxSubs.add(
            movieSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { movie: Content ->
                        val params = Bundle()
                        params.putString(Constants.SCREEN_NAME_KEY, selectedMovieTitle)
                        params.putSerializable(MOVIE_KEY, movie)
                        params.putSerializable(MOVIE_GENRES_KEY, genres as Serializable)
                        gotoMovie(params)
                    },
                    { t: Throwable ->
                        Timber.e("movieSelectObservable failed:$t")
                    }
                )
        )
    }

    private fun updateView() {
        viewDataBinding.moviesWidget.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppCompatTheme {
                    MoviesWidget(movies, arguments?.getString(TITLE) ?: getString(R.string.movies))
                }
            }
        }
//        setTitle(arguments?.getString(TITLE))
//        setSelectedMovieTitle(arguments?.getString(SELECTED_MOVIE_TITLE))
//        sectionTitleView.text = title
//        listView.adapter = adapter
//        listView.layoutManager =
//            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        adapter.contents = movies.toMutableList()
//        adapter.notifyDataSetChanged()
//
//        seeAllClickView.setOnClickListener {
//            context?.let {
//                ContentsActivity.startActivity(it, movies, R.string.movies)
//            }
//        }
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
        val intent = Intent(activity, MovieFragment::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}

@Composable
fun MoviesWidget(movies: List<Movie>, sectionTitle: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = sectionTitle,
                color = colorResource(R.color.color_white)
            )
            Row(horizontalArrangement = Arrangement.End) {
                Text(
                    stringResource(id = R.string.see_all),
                    color = colorResource(R.color.color_orange_app)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_keyboard_arrow_right_black_24dp),
                    contentDescription = null,
                    tint = colorResource(R.color.color_orange_app)
                )
            }
        }
        LazyRow {
            items(movies) { movie ->
                MovieItem(movie)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MovieItem(movie: Movie) {
    val imageUrl = UiUtils.getImageUrl(movie.posterPath, stringResource(id = R.string.image_small))
    Column {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
            ),
            contentDescription = null,
            modifier = Modifier
                .height(96.dp)
                .width(70.dp)
        )
        (movie.title ?: movie.originalTitle)?.let {
            Text(
                text = it,
                color = colorResource(R.color.color_white),
                maxLines = 1,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .widthIn(max = 100.dp)
            )
        }
        movie.releaseDate?.let {
            Text(
                text = it,
                color = colorResource(R.color.color_white),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
