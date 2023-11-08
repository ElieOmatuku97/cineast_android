package elieomatuku.cineast_android.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(stringResource(id = title).uppercase()) },
                    selected = state == index,
                    onClick = {
                        state = index
                    }
                )
            }
        }

        handleTabChange(tabs[state])
    }
}