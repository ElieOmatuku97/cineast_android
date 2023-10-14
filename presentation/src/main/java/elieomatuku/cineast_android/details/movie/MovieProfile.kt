package elieomatuku.cineast_android.details.movie

import android.util.TypedValue
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.details.Profile
import elieomatuku.cineast_android.domain.model.MovieSummary
import elieomatuku.cineast_android.utils.UiUtils
import me.zhanghai.android.materialratingbar.MaterialRatingBar

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
                        MaterialRatingBar(
                            it,
                            null,
                            me.zhanghai.android.materialratingbar.R.style.Widget_MaterialRatingBar_RatingBar//Widget_MaterialRatingBar_RatingBar
                        ).apply {
                            val params = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            params.height = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                10f,
                                resources.displayMetrics
                            ).toInt()
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