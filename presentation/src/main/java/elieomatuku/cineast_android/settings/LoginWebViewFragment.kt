package elieomatuku.cineast_android.settings

import elieomatuku.cineast_android.fragment.WebViewFragment

class LoginWebViewFragment : WebViewFragment() {
    private val viewModel: SettingsViewModel by sharedViewModel()

    override fun closeIconListener() {
        viewModel.getSession()
        super.closeIconListener()
    }
}
