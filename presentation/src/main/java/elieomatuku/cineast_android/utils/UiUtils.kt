package elieomatuku.cineast_android.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.Spanned
import android.text.style.URLSpan
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import elieomatuku.cineast_android.domain.model.Genre

object UiUtils {

    fun getImageUrl(path: String?, imageUrl: String?, fallBackImageUrl: String? = null): String {
        return getImageUri(imageUrl, fallBackImageUrl)
            .buildUpon()
            .appendEncodedPath(path)
            .toString()
    }

    private fun getImageUri(imageUrl: String?, fallBackImageUrl: String?): Uri {
        return if (imageUrl != null) {
            Uri.parse(imageUrl)
        } else {
            Uri.parse(fallBackImageUrl)
        }
    }

    fun initToolbar(activity: AppCompatActivity, toolbar: Toolbar?, showBack: Boolean = true) {
        activity.setSupportActionBar(toolbar)
        if (showBack) {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        activity.supportActionBar?.setDisplayUseLogoEnabled(true)
    }

    fun getYoutubeThumbnailPath(videoKey: String?, paramVideoSize: String?): String {
        return Uri.parse(Constants.YOUTUBE_URL)
            .buildUpon()
            .appendPath(Constants.PARAM_VIDEO)
            .appendPath(videoKey)
            .appendPath(paramVideoSize)
            .build()
            .toString()
    }

    fun mapMovieGenreIdsWithGenreNames(movieGenreIds: List<Int>, genres: List<Genre>): String? {
        val genresNames = getGenreNames(movieGenreIds, genres)
        return if ((genresNames != null) && genresNames.isNotEmpty()) {
            genresNames.joinToString(", ")
        } else {
            null
        }
    }

    fun retrieveNameFromGenre(genres: List<Genre>): String? {
        val genreNames: MutableList<String> = mutableListOf()
        genres.forEach {
            genreNames.add(it.name)
        }

        return genreNames.joinToString()
    }

    fun tintMenuItem(item: MenuItem, context: Context, colorRes: Int?) {
        val icon = item.icon
        if (colorRes != null)
            item.icon = getTintedDrawable(icon, context, colorRes)
    }

    fun getShareIntent(itemTitleOrName: String?, itemId: Int?, path: String? = null): Intent? {
        return if ((itemTitleOrName != null) && (itemId != null)) {
            configureShareIntent(itemTitleOrName, itemId, path)
        } else {
            null
        }
    }

    private fun configureShareIntent(
        itemTitleOrName: String?,
        itemId: Int?,
        path: String? = null
    ): Intent {
        return Intent()
            .setAction(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_SUBJECT, "Cineast - $itemTitleOrName")
            .putExtra(
                Intent.EXTRA_TEXT,
                "Check out $itemTitleOrName at TMDb.\n\n${ContentUtils.getContentUrl(itemId, path)}"
            )
            .setType("text/plain")
    }

    private fun getTintedDrawable(icon: Drawable, context: Context, color: Int): Drawable {
        val drawableCompat = DrawableCompat.wrap(icon)
        DrawableCompat.setTint(drawableCompat, ContextCompat.getColor(context, color))
        return drawableCompat
    }

    private fun getGenreNames(movieGenreIds: List<Int>, genres: List<Genre>): MutableList<String>? {
        val genresNames: MutableList<String> = mutableListOf()
        movieGenreIds.forEach { id ->
            genres.forEach {
                if (id == it.id)
                    genresNames.add(it.name)
            }
        }
        return genresNames
    }

    fun configureWebView(
        webView: WebView,
        progressListener: (progress: Int) -> Unit,
        progressVisibilityListener: (showProgress: Boolean) -> Unit
    ): WebView {
        val webv = webView

        webv.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressListener(newProgress)
            }
        }

        webv.webViewClient = object : WebViewClient() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.url?.toString()?.let {
                    view?.loadUrl(it)
                }
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    view?.loadUrl(it)
                }
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressVisibilityListener(true)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressVisibilityListener(false)
            }
        }

        webv.settings.loadsImagesAutomatically = true
        webv.settings.javaScriptEnabled = true
        webv.settings.layoutAlgorithm =
            WebSettings.LayoutAlgorithm.SINGLE_COLUMN // required for facebook
        webv.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webv.settings.setAppCacheEnabled(false)
        webv.clearCache(true)
        return webv
    }

    fun configSpannableLinkify(urlSpan: URLSpan, spannable: Spannable, linkSpan: URLSpan) {
        val spanStart = spannable.getSpanStart(urlSpan)
        val spanEnd = spannable.getSpanEnd(urlSpan)
        spannable.setSpan(linkSpan, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.removeSpan(urlSpan)
    }
}
