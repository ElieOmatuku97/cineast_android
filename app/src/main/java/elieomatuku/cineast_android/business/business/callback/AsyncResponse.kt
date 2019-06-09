package elieomatuku.cineast_android.business.business.callback

interface AsyncResponse<T> {
    fun onSuccess(result: T?)
    fun onFail(error: String)
}