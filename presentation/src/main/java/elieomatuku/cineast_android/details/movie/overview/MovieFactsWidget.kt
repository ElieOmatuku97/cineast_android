package elieomatuku.cineast_android.details.movie.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.MovieFacts

@Composable
fun MovieFactsWidget(movieFacts: MovieFacts?) {
    Column {
        Text(
            text = stringResource(id = R.string.movie_facts),
            fontSize = dimensionResource(id = R.dimen.toolbar_text_size).value.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                top = dimensionResource(id = R.dimen.holder_movie_layout_padding)
            ),
            color = colorResource(id = R.color.color_white)
        )
        Text(
            text = displayFacts(
                stringResource(id = R.string.release_date),
                movieFacts?.releaseDate
            ),
            fontSize = dimensionResource(id = R.dimen.holder_movie_facts_text_size).value.sp,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                top = dimensionResource(id = R.dimen.activity_margin_top),
                end = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right),
                bottom = dimensionResource(id = R.dimen.holder_movie_layout_padding)
            ),
            color = colorResource(id = R.color.color_white)
        )
        Text(
            text = displayFacts(
                stringResource(id = R.string.runtime),
                movieFacts?.runtimeInHoursAndMinutes
            ),
            fontSize = dimensionResource(id = R.dimen.holder_movie_facts_text_size).value.sp,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                top = dimensionResource(id = R.dimen.holder_item_movie_image_view_margin),
                end = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right),
                bottom = dimensionResource(id = R.dimen.holder_movie_layout_padding)
            ),
            color = colorResource(id = R.color.color_white)
        )
        Text(
            text = displayFacts(
                stringResource(id = R.string.budget),
                String.format("$%,.2f", movieFacts?.budget?.toDouble())
            ),
            fontSize = dimensionResource(id = R.dimen.holder_movie_facts_text_size).value.sp,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                top = dimensionResource(id = R.dimen.holder_item_movie_image_view_margin),
                end = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right),
                bottom = dimensionResource(id = R.dimen.holder_movie_layout_padding)
            ),
            color = colorResource(id = R.color.color_white)
        )
        Text(
            text = displayFacts(
                stringResource(id = R.string.revenue),
                String.format("$%,.2f", movieFacts?.revenue?.toDouble())
            ),
            fontSize = dimensionResource(id = R.dimen.holder_movie_facts_text_size).value.sp,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                top = dimensionResource(id = R.dimen.holder_item_movie_image_view_margin),
                end = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right),
                bottom = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right)
            ),
            color = colorResource(id = R.color.color_white)
        )
    }

}

private fun displayFacts(factName: String, factValue: String?): String {
    return factValue?.let {
        String.format("%s: %s", factName, it)
    } ?: String.format("%s: n/a", factName)
}
