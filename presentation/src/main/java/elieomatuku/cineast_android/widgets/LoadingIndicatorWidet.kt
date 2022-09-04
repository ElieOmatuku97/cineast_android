package elieomatuku.cineast_android.widgets

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import elieomatuku.cineast_android.R

/**
 * Created by elieomatuku on 2022-09-04
 */

@Composable
fun LoadingIndicatorWidget() {
    CircularProgressIndicator(
        color = colorResource(id = R.color.color_orange_app),
    )
}