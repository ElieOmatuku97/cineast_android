package elieomatuku.cineast_android.viewholder

import android.text.Spannable
import android.text.style.URLSpan
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.utils.UiUtils
import elieomatuku.cineast_android.utils.WebLink
import io.reactivex.subjects.PublishSubject

abstract class ProfileHolder(
    itemView: View,
    private val onProfileLinkClickedPublisher: PublishSubject<String>
) : RecyclerView.ViewHolder(itemView), WebLink<String> {

    fun linkify(spannable: Spannable): Spannable {
        val spans = spannable.getSpans(0, spannable.length, URLSpan::class.java)
        for (urlSpan in spans) {
            UiUtils.configSpannableLinkify(
                urlSpan, spannable,
                object : URLSpan(urlSpan.url) {
                    override fun onClick(view: View) {
                        gotoLink(url)
                    }
                }
            )
        }

        return spannable
    }

    override fun gotoLink(value: String) {
        onProfileLinkClickedPublisher.onNext(value)
    }
}
