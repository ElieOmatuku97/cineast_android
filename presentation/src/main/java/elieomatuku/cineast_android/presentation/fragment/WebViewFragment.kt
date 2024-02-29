package elieomatuku.cineast_android.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import elieomatuku.cineast_android.presentation.base.BaseFragment
import elieomatuku.cineast_android.presentation.widgets.WebView

class WebViewFragment : BaseFragment() {
    companion object {
        private const val URL = "url"
    }

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
                        closeIconListener()
                    }
                }
            }
        }
    }

    private fun closeIconListener() {
        findNavController().navigateUp()
    }
}