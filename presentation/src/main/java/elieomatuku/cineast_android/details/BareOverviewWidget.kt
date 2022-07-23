package elieomatuku.cineast_android.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import elieomatuku.cineast_android.R

@Composable
fun BareOverviewWidget(
    title: String = stringResource(id = R.string.plot_summary),
    overview: String = String()
) {
    Column {
        Text(
            text = title,
            color = colorResource(id = R.color.color_white),
            fontSize = dimensionResource(id = R.dimen.toolbar_text_size).value.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.holder_movie_layout_padding),
                    start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                )
        )
        Text(
            text = overview,
            color = colorResource(id = R.color.color_white),
            fontSize = dimensionResource(id = R.dimen.holder_movie_facts_text_size).value.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                    start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                    end = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right),
                    bottom = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right)
                )
        )
    }
}
