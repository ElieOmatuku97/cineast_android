package elieomatuku.cineast_android.utils

import elieomatuku.cineast_android.domain.model.CineastError

object ApiUtils {
    fun throwableToCineastError(throwable: Throwable?): CineastError {
        return CineastError(throwable.toString())
    }
}
