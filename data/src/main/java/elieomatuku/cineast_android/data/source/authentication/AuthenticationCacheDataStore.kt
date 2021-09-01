package elieomatuku.cineast_android.data.source.authentication

import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.AccountEntity
import elieomatuku.cineast_android.data.model.SessionEntity
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationCache
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationDataStore

/**
 * Created by elieomatuku on 2021-08-22
 */

class AuthenticationCacheDataStore(private val authenticationCache: AuthenticationCache) :
    AuthenticationDataStore {
    override suspend fun getAccessToken(): AccessTokenEntity {
        return authenticationCache.getAccessToken()
    }

    override suspend fun getSession(requestToken: String): SessionEntity {
        TODO("Not yet implemented")
    }

    override suspend fun setAccount(sessionId: String?): AccountEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getRequestToken(): String? {
        return authenticationCache.getRequestToken()
    }

    override suspend fun getUsername(): String? {
        return authenticationCache.getUsername()
    }

    override suspend fun logout() {
        authenticationCache.logout()
    }

    override suspend fun isLoggedIn(): Boolean {
        return authenticationCache.isLoggedIn()
    }
}
