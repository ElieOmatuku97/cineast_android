package elieomatuku.cineast_android.remote

import elieomatuku.cineast_android.remote.api.AuthenticationApi
import elieomatuku.cineast_android.remote.model.RemoteAccessToken
import elieomatuku.cineast_android.remote.model.RemoteAccount
import elieomatuku.cineast_android.remote.model.RemoteException
import elieomatuku.cineast_android.remote.model.RemoteSession

/**
 * Created by elieomatuku on 2021-07-04
 */

class AuthenticationRemoteImpl(private val authenticationApi: AuthenticationApi) {

    suspend fun getAccessToken(): RemoteAccessToken {
        val response = authenticationApi.getAccessToken()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getSession(requestToken: String?): RemoteSession {
        val response = authenticationApi.getSession(requestToken)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun setAccount(sessionId: String?): RemoteAccount {
        val response = authenticationApi.getAccount(sessionId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }
}
