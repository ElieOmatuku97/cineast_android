package elieomatuku.cineast_android.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elieomatuku.cineast_android.presentation.base.BaseFragment
import elieomatuku.cineast_android.presentation.widgets.LoadingIndicatorWidget
import elieomatuku.cineast_android.presentation.widgets.WebView
import kotlinx.coroutines.launch

class LoginWebViewFragment : BaseFragment() {
    companion object {
        private const val URL = "url"
        private const val REQUEST_TOKEN = "request_token"
    }

    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val url = arguments?.getString(URL)
        arguments?.getString(REQUEST_TOKEN)?.let {
            viewModel.setRequestToken(it)
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LoginWebViewScreen(
                    viewModel = viewModel,
                    navigateUp = { findNavController().navigateUp() }
                ) {
                    if (!url.isNullOrEmpty()) {
                        WebView(url = url) {
                            viewModel.getSession()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginWebViewScreen(
    viewModel: SettingsViewModel,
    navigateUp: () -> Unit,
    child: @Composable () -> Unit = {},
) {
    val viewState by viewModel.viewState.observeAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) {
        val paddingValues = it
        viewState?.apply {
            Box(modifier = Modifier.fillMaxSize()) {
                child()
            }

            viewError?.consume()?.apply {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LaunchedEffect(message) {
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                    navigateUp()
                }
            }

            if (isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingIndicatorWidget()
                }
            }

            if (isLoggedIn) {
                navigateUp()
            }
        }
    }
}
