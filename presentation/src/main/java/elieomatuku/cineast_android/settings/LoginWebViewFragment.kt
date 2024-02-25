package elieomatuku.cineast_android.settings

import androidx.fragment.app.activityViewModels
import elieomatuku.cineast_android.fragment.WebViewFragment

class LoginWebViewFragment : WebViewFragment() {

    private val viewModel: SettingsViewModel by activityViewModels()

    override fun closeIconListener() {
        viewModel.getSession()
        super.closeIconListener()
    }
}
