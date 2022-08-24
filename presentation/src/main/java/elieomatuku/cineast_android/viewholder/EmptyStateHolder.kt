package elieomatuku.cineast_android.viewholder

import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.recyclerview.widget.RecyclerView
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.connection.ConnectionService
import org.kodein.di.Kodein
import org.kodein.di.android.closestKodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class EmptyStateHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView),
    KodeinAware {
    companion object {
        private fun createComposeView(parent: ViewGroup): ComposeView {
            return ComposeView(parent.context)
        }

        fun newInstance(parent: ViewGroup): EmptyStateHolder {
            return EmptyStateHolder(createComposeView(parent))
        }
    }

    override val kodein: Kodein by closestKodein(itemView.context)
    private val connectionService: ConnectionService by instance()

    fun update(errorMsg: String?) {
        composeView.setContent {
            AppCompatTheme {
                EmptyStateItem(
                    errorMsg = errorMsg,
                    hasNetworkConnection = connectionService.hasNetworkConnection
                )
            }
        }
    }
}

@Composable
fun EmptyStateItem(
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
            color = colorResource(id = R.color.color_white),
            fontFamily = FontFamily(listOf(Font(R.font.barlow_bold))),
            fontSize = 20.sp
        )

        Text(
            text = errorMsg?.uppercase()
                ?: stringResource(id = R.string.empty_state_no_internet_msg),
            color = colorResource(id = R.color.color_white),
            fontFamily = FontFamily(listOf(Font(R.font.barlow_bold))),
            fontSize = dimensionResource(id = R.dimen.login_text_size).value.sp,
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
                .padding(top = 8.dp),
            tint = colorResource(id = R.color.color_accent)
        )
    }
}
