package elieomatuku.cineast_android.discover

import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.recyclerview.widget.RecyclerView
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R

class LoginViewHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView) {
    companion object {
        private fun createComposeView(parent: ViewGroup): ComposeView {
            return ComposeView(parent.context)
        }

        fun newInstance(parent: ViewGroup): LoginViewHolder {
            return LoginViewHolder(createComposeView(parent))
        }
    }

    fun update(isLoggedIn: Boolean) {
        composeView.setContent {
            AppCompatTheme {
                LoginItem(isLoggedIn = isLoggedIn)
            }
        }
    }
}

@Composable
fun LoginItem(
    isLoggedIn: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isLoggedIn) {
            Text(
                text = stringResource(id = R.string.not_logged_in),
                color = colorResource(id = R.color.color_grey),
                fontSize = dimensionResource(id = R.dimen.holder_movie_facts_text_size).value.sp
            )
        }
        Text(
            text = stringResource(id = if (isLoggedIn) R.string.settings_logout else R.string.settings_login),
            color = colorResource(id = R.color.color_orange_app),
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(id = R.dimen.login_text_size).value.sp,
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.activity_margin_top),
                    bottom = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                )

        )
    }
}
