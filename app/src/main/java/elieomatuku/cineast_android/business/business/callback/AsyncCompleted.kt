package elieomatuku.restapipractice.business.business.callback

interface AsyncCompleted: AsyncResponse<Nothing> {

    override fun onSuccess(result: Nothing?) {
        onSuccess()
    }

    fun onSuccess()
}