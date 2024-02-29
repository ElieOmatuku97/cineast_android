package elieomatuku.cineast_android.presentation.details.movie

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.presentation.details.Profile
import elieomatuku.cineast_android.presentation.utils.UiUtils

@Composable
fun MovieProfile(
    movieSummary: MovieSummary,
    onProfileClick: (Int) -> Unit,
    onRateClick: () -> Unit,
    gotoLink: (String) -> Unit
) {
    val movie = movieSummary.movie
    val genres = movieSummary.genres
    val genresIds = movie?.genreIds
    val genresNames = if (genresIds != null && genres != null) {
        UiUtils.mapMovieGenreIdsWithGenreNames(genresIds, genres)
    } else {
        movie?.genres?.run {
            UiUtils.retrieveNameFromGenre(this)
        }
    }
    val homepage = movieSummary.facts?.homepage

    Column {
        Profile(
            imagePath = movie?.imagePath,
            title = movie?.title,
            subTitle = movie?.releaseDate,
            description = genresNames,
            webSiteLink = homepage,
            onProfileClick = {
                movie?.let {
                    onProfileClick(it.id)
                }
            },
            onWebSiteLinkClick = {
                gotoLink(it)
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.padding_small)
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                movie?.voteAverage?.let { voteAverage ->
                    AndroidView(factory = {
                        AppCompatRatingBar(
                            it,
                            null,
                            androidx.legacy.preference.R.attr.ratingBarStyleSmall
                        ).apply {
                            val params = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            rating = 2f
                            scaleX = 0.7f
                            scaleY = 0.65f
                            pivotX = 0f
                            pivotY = 0f
                            layoutParams = params

                            numStars = 10
                            stepSize = 0.1f

                            rating = voteAverage
                        }
                    })
                }

                if (!movieSummary.isEmpty()) {
                    Text(
                        stringResource(id = R.string.rate),
                        fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                        modifier = Modifier
                            .padding(end = dimensionResource(id = R.dimen.padding_xlarge))
                            .clickable(onClick = { onRateClick() })
                    )
                }
            }
        }
    }
}