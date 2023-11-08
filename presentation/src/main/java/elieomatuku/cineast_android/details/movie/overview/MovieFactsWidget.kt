package elieomatuku.cineast_android.details.movie.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.MovieFacts

@Composable
fun MovieFactsWidget(movieFacts: MovieFacts?) {
    Surface {
        Column {
            Text(
                text = stringResource(id = R.string.movie_facts),
                fontSize = dimensionResource(id = R.dimen.text_size_large).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_small),
                    top = dimensionResource(id = R.dimen.padding_small)
                )
            )
            Text(
                text = displayFacts(
                    stringResource(id = R.string.release_date),
                    movieFacts?.releaseDate
                ),
                fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_small),
                    top = dimensionResource(id = R.dimen.padding_small),
                    end = dimensionResource(id = R.dimen.padding_large),
                    bottom = dimensionResource(id = R.dimen.padding_small)
                )
            )
            Text(
                text = displayFacts(
                    stringResource(id = R.string.runtime),
                    movieFacts?.runtimeInHoursAndMinutes
                ),
                fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_small),
                    top = dimensionResource(id = R.dimen.padding_xsmall),
                    end = dimensionResource(id = R.dimen.padding_large),
                    bottom = dimensionResource(id = R.dimen.padding_small)
                )
            )
            Text(
                text = displayFacts(
                    stringResource(id = R.string.budget),
                    String.format("$%,.2f", movieFacts?.budget?.toDouble())
                ),
                fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_small),
                    top = dimensionResource(id = R.dimen.padding_xsmall),
                    end = dimensionResource(id = R.dimen.padding_large),
                    bottom = dimensionResource(id = R.dimen.padding_small)
                )
            )
            Text(
                text = displayFacts(
                    stringResource(id = R.string.revenue),
                    String.format("$%,.2f", movieFacts?.revenue?.toDouble())
                ),
                fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_small),
                    top = dimensionResource(id = R.dimen.padding_xsmall),
                    end = dimensionResource(id = R.dimen.padding_large),
                    bottom = dimensionResource(id = R.dimen.padding_large)
                )
            )
        }
    }

}

private fun displayFacts(factName: String, factValue: String?): String {
    return factValue?.let {
        String.format("%s: %s", factName, it)
    } ?: String.format("%s: n/a", factName)
}
