package elieomatuku.cineast_android.details.movie.overview

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.domain.model.Trailer

@Composable
fun MovieOverviewWidget(
    overviewTitle: String,
    movieSummary: MovieSummary,
    onTrailerClick: (trailer: Trailer) -> Unit,
    bareOverviewComposable: @Composable (title: String, overview: String) -> Unit
) {
    LazyColumn {
        item {
            bareOverviewComposable(
                title = overviewTitle,
                overview = movieSummary.movie?.overview ?: String()
            )
        }
        item {
            TrailersWidget(
                trailers = movieSummary.trailers ?: emptyList(),
                onItemClick = onTrailerClick
            )
        }
        item { MovieFactsWidget(movieFacts = movieSummary.facts) }
    }
}
