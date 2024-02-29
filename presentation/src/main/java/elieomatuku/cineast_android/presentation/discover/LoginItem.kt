package elieomatuku.cineast_android.presentation.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import elieomatuku.cineast_android.R

@Composable
fun LoginItem(
    isLoggedIn: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick() }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isLoggedIn) {
            Text(
                text = stringResource(id = R.string.not_logged_in),
                fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp
            )
        }
        Text(
            text = stringResource(id = if (isLoggedIn) R.string.settings_logout else R.string.settings_login),
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(id = R.dimen.text_size_large).value.sp,
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.padding_small),
                    bottom = dimensionResource(id = R.dimen.padding_small),
                )

        )
    }
}
