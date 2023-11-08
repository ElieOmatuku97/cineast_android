package elieomatuku.cineast_android.settings

import androidx.fragment.app.viewModels
import elieomatuku.cineast_android.fragment.WebViewFragment

class LoginWebViewFragment : WebViewFragment() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun closeIconListener() {
        viewModel.getSession()
        super.closeIconListener()
    }
}
