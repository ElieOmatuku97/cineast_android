package elieomatuku.cineast_android.remote

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody

/**
 * Created by elieomatuku on 2021-08-08
 */

object RemoteUtils {

    const val MOVIE = "movieApi"

    fun <T> getRequestBody(item: T): RequestBody {
        val mediaType = "application/json".toMediaType()
        return RequestBody.create(mediaType, toJson(item))
    }

    private fun <T> toJson(item: T): String {
        val gson = Gson()

        return gson.toJson(item)
    }
}
