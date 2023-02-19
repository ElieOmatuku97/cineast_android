package elieomatuku.cineast_android.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import elieomatuku.cineast_android.R

@Composable
fun DetailTabs(
    tabs: List<Int>,
    handleTabChange: @Composable (Int) -> Unit
) {
    var state by remember { mutableStateOf(0) }
    Column {
        TabRow(
            selectedTabIndex = state,
            contentColor = colorResource(id = R.color.color_orange_app),
            backgroundColor = colorResource(id = R.color.color_black_app),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(stringResource(id = title).uppercase()) },
                    selected = state == index,
                    onClick = {
                        state = index
                    },
                    selectedContentColor = colorResource(id = R.color.color_orange_app),
                    unselectedContentColor = colorResource(id = R.color.color_grey_app),
                )
            }
        }

        handleTabChange(tabs[state])
    }
}