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
            fontSize = dimensionResource(id = R.dimen.text_size_large).value.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.padding_small),
                    start = dimensionResource(id = R.dimen.padding_small),
                )
        )
        Text(
            text = overview,
            fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.padding_small),
                    start = dimensionResource(id = R.dimen.padding_small),
                    end = dimensionResource(id = R.dimen.padding_large),
                    bottom = dimensionResource(id = R.dimen.padding_large)
                )
        )
    }
}
