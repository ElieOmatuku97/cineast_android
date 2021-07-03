package elieomatuku.cineast_android.utils

import elieomatuku.cineast_android.business.client.MoshiSerializer
import elieomatuku.cineast_android.business.client.Serializer
import elieomatuku.cineast_android.domain.model.CineastError
import okhttp3.ResponseBody

object ApiUtils {
    private val errorSerializer: Serializer<CineastError> by lazy {
        MoshiSerializer<CineastError>(CineastError::class.java)
    }

    fun throwableToCineastError(throwable: Throwable?): CineastError {
        return CineastError(throwable.toString())
    }

    fun throwableToCineastError(errorBody: ResponseBody?): CineastError {
        errorBody?.let {
            val cineastError = errorSerializer.fromJson(it.string())

            if (cineastError != null) {
                return cineastError
            }
        }

        return CineastError()
    }
}
