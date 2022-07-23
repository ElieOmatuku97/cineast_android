package elieomatuku.cineast_android.details.movie.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.databinding.FragmentMovieOverviewBinding
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.details.BareOverviewWidget
import elieomatuku.cineast_android.details.movie.MovieFragmentDirections
import elieomatuku.cineast_android.domain.model.Trailer

class MovieOverviewFragment : BaseFragment() {
    companion object {
        private const val OVERVIEW_SUMMARY = "overview_movie_summary"
        private const val OVERVIEW_TITLE = "overview_title"

        fun newInstance(overviewTitle: String, movieSummary: MovieSummary): MovieOverviewFragment {
            val args = Bundle()
            args.putString(OVERVIEW_TITLE, overviewTitle)
            args.putSerializable(OVERVIEW_SUMMARY, movieSummary)
            val fragment = MovieOverviewFragment()
            fragment.arguments = args

            return fragment
        }
    }

    private var _binding: FragmentMovieOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieSummary: MovieSummary? = arguments?.get(OVERVIEW_SUMMARY) as MovieSummary?
        val overviewTitle: String = arguments?.getString(OVERVIEW_TITLE) ?: String()

        binding.movieOverviewWidget.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        binding.movieOverviewWidget.setContent {
            AppCompatTheme {
                movieSummary?.let {
                    MovieOverviewWidget(
                        overviewTitle = overviewTitle,
                        movieSummary = movieSummary,
                        onTrailerClick = { trailer ->
                            trailer.key?.let {
                                showTrailer(it)
                            }
                        }) { title, overview ->
                        BareOverviewWidget(title = title, overview = overview)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showTrailer(trailerKey: String) {
        val directions = MovieFragmentDirections.navigateToVideo(trailerKey)
        findNavController().navigate(directions)
    }
}

@Composable
fun MovieOverviewWidget(
    overviewTitle: String,
    movieSummary: MovieSummary,
    onTrailerClick: (trailer: Trailer) -> Unit,
    bareOverviewComposable: @Composable (title: String, overview: String) -> Unit
) {
    Column {
        bareOverviewComposable(
            title = overviewTitle,
            overview = movieSummary.movie?.overview ?: String()
        )
        TrailersWidget(
            trailers = movieSummary.trailers ?: emptyList(),
            onItemClick = onTrailerClick
        )
        MovieFactsWidget(movieFacts = movieSummary.facts)
    }
}
