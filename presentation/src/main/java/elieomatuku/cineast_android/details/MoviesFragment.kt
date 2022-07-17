package elieomatuku.cineast_android.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.databinding.FragmentMoviesBinding
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.commonwidgets.MoviesWidget
import elieomatuku.cineast_android.contents.ContentsActivity
import elieomatuku.cineast_android.details.movie.MovieFragment
import elieomatuku.cineast_android.utils.Constants
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

    private lateinit var movies: List<Movie>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding =
            FragmentMoviesBinding.bind(inflater.inflate(R.layout.fragment_movies, container, false))

        arguments?.getSerializable(MOVIES)?.let {
            movies = it as List<Movie>
        }

        updateView()

        return viewDataBinding.root
    }

    private fun updateView() {
        viewDataBinding.moviesWidget.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppCompatTheme {
                    MoviesWidget(
                        movies = movies,
                        sectionTitle = arguments?.getString(SELECTED_MOVIE_TITLE)
                            ?: arguments?.getString(TITLE)
                            ?: getString(R.string.movies),
                        onItemClick = { content, genres ->
                            val params = Bundle()
                            if (content is Movie) {
                                params.putString(Constants.SCREEN_NAME_KEY, content.title)
                            }
                            params.putSerializable(MOVIE_KEY, content)
                            params.putSerializable(MOVIE_GENRES_KEY, genres as Serializable)
                            gotoMovie(params)
                        },
                        onSeeAllClick = {
                            context?.let {
                                ContentsActivity.startActivity(it, movies, R.string.movies)
                            }
                        }
                    )
                }
            }
        }
    }

    private fun gotoMovie(params: Bundle) {
        val intent = Intent(activity, MovieFragment::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}