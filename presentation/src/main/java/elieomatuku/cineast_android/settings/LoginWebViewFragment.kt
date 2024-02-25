package elieomatuku.cineast_android.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.widgets.WebView

class LoginWebViewFragment : BaseFragment() {
    companion object {
        private const val URL = "url"
    }

    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val url = arguments?.getString(URL)
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                if (!url.isNullOrEmpty()) {
                    WebView(url = url) {
                        onComplete()
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun onComplete() {
        viewModel.getSession()
    }
}
