package elieomatuku.cineast_android.ui.utils

import android.net.Uri

object ContentUtils {

    private const val URL =
        "https://www.themoviedb.org/" // should put that in preference or maybe in the depInject

    fun supportsShare(itemId: Int?): Boolean {
        return itemId != null
    }

    fun getContentUrl(contentId: Int?, path: String? = null): String {

        return Uri.parse(URL)
            .buildUpon()
            .appendPath(path ?: "movieApi")
            .appendPath(contentId.toString())
            .build()
            .toString()
    }
}
