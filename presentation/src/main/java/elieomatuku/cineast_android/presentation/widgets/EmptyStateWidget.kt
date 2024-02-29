package elieomatuku.cineast_android.presentation.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import elieomatuku.cineast_android.presentation.R

@Composable
fun EmptyStateWidget(
    errorMsg: String?,
    hasNetworkConnection: Boolean
) {
    Column(
        modifier = Modifier
            .aspectRatio(9 / 16f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (hasNetworkConnection) stringResource(id = R.string.no_content_title) else "No Internet",
            fontFamily = FontFamily(listOf(Font(R.font.barlow_bold))),
            fontSize = 20.sp
        )

        Text(
            text = errorMsg?.uppercase()
                ?: stringResource(id = R.string.empty_state_no_internet_msg),
            fontFamily = FontFamily(listOf(Font(R.font.barlow_bold))),
            fontSize = dimensionResource(id = R.dimen.text_size_large).value.sp,
            modifier = Modifier
                .padding(
                    top = 20.dp,
                    end = 20.dp,
                    start = 20.dp
                )
        )

        Icon(
            painter = painterResource(id = if (hasNetworkConnection) R.drawable.ic_movie_black_24dp else R.drawable.ic_signal_wifi_off_black_24dp),
            contentDescription = null,
            modifier = Modifier
                .width(35.dp)
                .height(35.dp)
                .padding(top = 8.dp)
        )
    }
}
