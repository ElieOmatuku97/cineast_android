package elieomatuku.cineast_android.viewholder.itemHolder

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.text.Spannable
import android.text.Spanned
import android.text.style.URLSpan
import android.view.View
import elieomatuku.cineast_android.fragment.WebviewFragment
import elieomatuku.cineast_android.utils.WebLink


abstract class ProfileHolder(itemView: View) : RecyclerView.ViewHolder(itemView), WebLink<String> {


    fun linkify(spannable: Spannable): Spannable {
        val spans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
        for (urlSpan in spans) {
            configSpannableLinkify(urlSpan, spannable, object : URLSpan(urlSpan.url) {
                override fun onClick(view: View) {
                    gotoWebview(url)
                }
            })
        }

        return spannable
    }


    fun configSpannableLinkify(urlSpan: URLSpan, spannable: Spannable, linkSpan: URLSpan) {
        val spanStart = spannable.getSpanStart(urlSpan)
        val spanEnd = spannable.getSpanEnd(urlSpan)
        spannable.setSpan(linkSpan, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.removeSpan(urlSpan)
    }


    override fun gotoWebview(value: String) {
        val webviewFragment: WebviewFragment? = WebviewFragment.newInstance(value)
        val fm = (itemView.context as AppCompatActivity).supportFragmentManager

        if (webviewFragment != null && fm != null) {
            fm.beginTransaction().add(android.R.id.content, webviewFragment, null).addToBackStack(null).commit()
        }
    }
}