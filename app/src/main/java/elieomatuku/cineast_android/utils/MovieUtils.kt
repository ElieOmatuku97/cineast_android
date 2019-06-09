package elieomatuku.restapipractice.utils

import android.net.Uri
import elieomatuku.restapipractice.business.business.model.data.Movie

object MovieUtils {

    private const val TMDbUrl = "https://www.themoviedb.org/"  // should put that in preference or maybe in the depInject

    fun supportsShare(itemId: Int?): Boolean {
        return itemId != null
    }

    fun getMovieUrl(movieId: Int?, tmdbPath: String? = null): String {
        val url = Uri.parse(TMDbUrl)
                .buildUpon()
                .appendPath(tmdbPath?: "movie")
                .appendPath(movieId.toString())
                .build()
                .toString()

        return url

    }
}