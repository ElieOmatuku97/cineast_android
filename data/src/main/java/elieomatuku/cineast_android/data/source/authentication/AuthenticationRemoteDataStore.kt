package elieomatuku.cineast_android.data.source.authentication

import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.AccountEntity
import elieomatuku.cineast_android.data.model.SessionEntity
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationDataStore
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationRemote

/**
 * Created by elieomatuku on 2021-08-22
 */

class AuthenticationRemoteDataStore(private val authenticationRemote: AuthenticationRemote) :
    AuthenticationDataStore {
    override suspend fun getAccessToken(): AccessTokenEntity {
        return authenticationRemote.getAccessToken()
    }

    override suspend fun getSession(requestToken: String): SessionEntity {
        return authenticationRemote.getSession(requestToken)
    }

    override suspend fun setAccount(sessionId: String?): AccountEntity {
        return authenticationRemote.setAccount(sessionId)
    }

    override suspend fun getRequestToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun getUsername(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun isLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }
}
