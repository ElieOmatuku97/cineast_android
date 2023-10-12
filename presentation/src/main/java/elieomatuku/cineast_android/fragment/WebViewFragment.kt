package elieomatuku.cineast_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.fragment.findNavController
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.utils.UiUtils

open class WebViewFragment : BaseFragment() {
    companion object {
        private const val URL = "url"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val url = arguments?.getString(URL)
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                if (!url.isNullOrEmpty()) {
                    WebView(url = url) {
                        closeIconListener()
                    }
                }
            }
        }
    }

    open fun closeIconListener() {
        findNavController().navigateUp()
    }
}

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
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }
        AndroidView(
            factory = {
                WebView(it).let {
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