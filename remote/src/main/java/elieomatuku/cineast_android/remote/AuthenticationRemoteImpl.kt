package elieomatuku.cineast_android.remote

import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.AccountEntity
import elieomatuku.cineast_android.data.model.SessionEntity
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationRemote
import elieomatuku.cineast_android.remote.api.AuthenticationApi
import elieomatuku.cineast_android.remote.model.RemoteAccessToken
import elieomatuku.cineast_android.remote.model.RemoteAccount
import elieomatuku.cineast_android.remote.model.RemoteException
import elieomatuku.cineast_android.remote.model.RemoteSession

/**
 * Created by elieomatuku on 2021-07-04
 */

class AuthenticationRemoteImpl(private val authenticationApi: AuthenticationApi) :
    AuthenticationRemote {

    override suspend fun getAccessToken(): AccessTokenEntity {
        val response = authenticationApi.getAccessToken()
        if (response.isSuccessful) {
            val body = response.body()
            return RemoteAccessToken.toAccessTokenEntity(body!!)
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
            return RemoteSession.toSessionEntity(body!!)
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getAccount(sessionId: String?): AccountEntity {
        val response = authenticationApi.getAccount(sessionId)
        if (response.isSuccessful) {
            val body = response.body()
            return RemoteAccount.toAccountEntity(body!!)
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }
}
