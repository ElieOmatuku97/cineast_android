package elieomatuku.cineast_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.databinding.FragmentGalleryBinding
import elieomatuku.cineast_android.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_webview.view.*

open class WebViewFragment : BaseFragment() {
    companion object {
        const val URL = "url"

        fun newInstance(url: String?): WebViewFragment {
            val fragment = WebViewFragment()
            val args = Bundle()
            url?.let {
                args.putString(URL, it)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_webview, container, false)
        val url = arguments?.getString(URL)

        val progressBar = view.web_progress

        val webView by lazy {
            val webV = view.web_html_widget
            UiUtils.configureWebView(webV, progressBar)
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    view?.loadUrl(it)
                }
                return true
            }
        }

        if (!url.isNullOrEmpty()) {
            webView.loadUrl(url)
        }

        view.html_close_icon.setOnClickListener { view ->
            closeIconListener()
        }

        return view
    }

    open fun closeIconListener() {
        findNavController().navigateUp()
    }
}
