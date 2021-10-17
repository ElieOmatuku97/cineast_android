package elieomatuku.cineast_android.ui.viewholder

import android.text.Spannable
import android.text.style.URLSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.ui.fragment.WebViewFragment
import elieomatuku.cineast_android.ui.utils.UiUtils
import elieomatuku.cineast_android.ui.utils.WebLink

abstract class ProfileHolder(itemView: View) : RecyclerView.ViewHolder(itemView), WebLink<String> {

    fun linkify(spannable: Spannable): Spannable {
        val spans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
        for (urlSpan in spans) {
            UiUtils.configSpannableLinkify(
                urlSpan, spannable,
                object : URLSpan(urlSpan.url) {
                    override fun onClick(view: View) {
                        gotoWebview(url)
                    }
                }
            )
        }

        return spannable
    }

    override fun gotoWebview(value: String) {
        val webViewFragment: WebViewFragment = WebViewFragment.newInstance(value)
        val fm = (itemView.context as AppCompatActivity).supportFragmentManager
        fm.beginTransaction().add(android.R.id.content, webViewFragment, null).addToBackStack(null).commit()
    }
}
