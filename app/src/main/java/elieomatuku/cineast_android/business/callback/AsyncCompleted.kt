package elieomatuku.cineast_android.business.callback

interface AsyncCompleted: AsyncResponse<Nothing> {

    override fun onSuccess(result: Nothing?) {
        onSuccess()
    }

    fun onSuccess()
}