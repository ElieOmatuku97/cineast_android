package elieomatuku.cineast_android.remote.model

/**
 * Created by elieomatuku on 2021-06-13
 */

data class RemoteException(
    val code: Int,
    val errorBody: String?,
    val msg: String
) : RuntimeException(msg)
