package elieomatuku.cineast_android.ui.settings

import android.os.Bundle
import elieomatuku.cineast_android.ui.fragment.WebViewFragment

class LoginWebViewFragment : WebViewFragment() {
    companion object {
        private const val URL = "url"

        fun newInstance(url: String?): WebViewFragment {
            val fragment = LoginWebViewFragment()
            val args = Bundle()
            url?.let {
                args.putString(URL, it)
            }

            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: SettingsViewModel by sharedViewModel<SettingsViewModel>()

    override fun closeIconListener() {
        viewModel.getSession()
        super.closeIconListener()
    }
}
