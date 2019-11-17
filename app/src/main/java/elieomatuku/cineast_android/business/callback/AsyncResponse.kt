package elieomatuku.cineast_android.business.callback

import elieomatuku.cineast_android.business.model.data.CineastError

interface AsyncResponse<T> {
    fun onSuccess(result: T?)
    fun onFail(error: CineastError)
}