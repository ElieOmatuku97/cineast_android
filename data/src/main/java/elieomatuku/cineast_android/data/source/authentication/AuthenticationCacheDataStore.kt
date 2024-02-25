package elieomatuku.cineast_android.data.source.authentication

import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.AccountEntity
import elieomatuku.cineast_android.data.model.SessionEntity
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationCache
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationDataStore
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class AuthenticationCacheDataStore @Inject constructor (private val authenticationCache: AuthenticationCache) :
    AuthenticationDataStore {
    override suspend fun getAccessToken(): AccessTokenEntity {
        return authenticationCache.getAccessToken()
    }

    override suspend fun setAccessToken(accessTokenEntity: AccessTokenEntity) {
        authenticationCache.setAccessToken(accessTokenEntity)
    }

    override suspend fun setSession(sessionEntity: SessionEntity) {
        authenticationCache.setSession(sessionEntity)
    }

    override suspend fun getSession(requestToken: String): SessionEntity {
        return authenticationCache.getSession()
    }

    override suspend fun setAccount(accountEntity: AccountEntity) {
        authenticationCache.setAccount(accountEntity)
    }

    override suspend fun getAccount(sessionId: String?): AccountEntity {
        return authenticationCache.getAccount() ?: throw Exception()
    }

    override suspend fun getAccount(): AccountEntity? {
        return authenticationCache.getAccount()
    }

    override suspend fun logout() {
        authenticationCache.logout()
    }

    override suspend fun isLoggedIn(): Boolean {
        return authenticationCache.isLoggedIn()
    }
}
