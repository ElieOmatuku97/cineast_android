package elieomatuku.cineast_android.remote

import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.AccountEntity
import elieomatuku.cineast_android.data.model.SessionEntity
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationRemote
import elieomatuku.cineast_android.remote.api.AuthenticationApi
import elieomatuku.cineast_android.remote.model.RemoteException

/**
 * Created by elieomatuku on 2021-07-04
 */

class AuthenticationRemoteImpl(private val authenticationApi: AuthenticationApi) :
    AuthenticationRemote {

    override suspend fun getAccessToken(): AccessTokenEntity {
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

    override suspend fun getSession(requestToken: String?): SessionEntity {
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

    override suspend fun setAccount(sessionId: String?): AccountEntity {
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
