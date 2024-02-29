package elieomatuku.cineast_android.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import elieomatuku.cineast_android.presentation.utils.UiUtils

@Composable
fun WebView(url: String, onCloseButtonClick: () -> Unit) {
    Column {
        var progress by rememberSaveable { mutableStateOf(0) }
        var showProgress by remember { mutableStateOf(true) }

        if (showProgress) {
            LinearProgressIndicator(
                progress = progress.toFloat() / 10
            )
        }

        Row(
            modifier = Modifier
                .background(color = Color.Black)
                .align(Alignment.End)
        ) {
            IconButton(onClick = { onCloseButtonClick() }) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null
                )
            }
        }
        AndroidView(
            factory = {
                android.webkit.WebView(it).let {
                    val webView by lazy {
                        UiUtils.configureWebView(
                            it,
                            {
                                progress = it
                            }, {
                                showProgress = it
                            }
                        )
                    }

                    webView.loadUrl(url)

                    webView
                }
            }
        )
    }
}