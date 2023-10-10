package elieomatuku.cineast_android.settings

import elieomatuku.cineast_android.fragment.WebViewFragment
import javax.inject.Inject

class LoginWebViewFragment : WebViewFragment() {

    @Inject
    lateinit var viewModel: SettingsViewModel

    override fun closeIconListener() {
        viewModel.getSession()
        super.closeIconListener()
    }
}
