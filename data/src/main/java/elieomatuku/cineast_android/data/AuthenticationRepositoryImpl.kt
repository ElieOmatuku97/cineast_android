package elieomatuku.cineast_android.data

import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.AccountEntity
import elieomatuku.cineast_android.data.model.SessionEntity
import elieomatuku.cineast_android.data.source.authentication.AuthenticationDataStoreFactory
import elieomatuku.cineast_android.data.source.authentication.AuthenticationRemoteDataStore
import elieomatuku.cineast_android.domain.model.AccessToken
import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.domain.model.Session
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository

/**
 * Created by elieomatuku on 2021-08-22
 */

class AuthenticationRepositoryImpl(private val factory: AuthenticationDataStoreFactory) :
    AuthenticationRepository {

    override suspend fun getAccessToken(): AccessToken {
        val dataStore = factory.retrieveDataStore()
        val accessToken = dataStore.getAccessToken()
        if (dataStore is AuthenticationRemoteDataStore) {
            factory.retrieveCacheDataStore().setAccessToken(accessToken)
        }
        return accessToken.let(AccessTokenEntity::toAccessToken)
    }

    override suspend fun getSession(requestToken: String): Session {
        return factory.retrieveRemoteDataStore().getSession(requestToken)
            .let(SessionEntity::toSession)
    }

    override suspend fun setAccount(sessionId: String?): Account {
        return factory.retrieveRemoteDataStore().setAccount(sessionId).let(AccountEntity::toAccount)
    }

    override suspend fun getRequestToken(): String? {
        return factory.retrieveCacheDataStore().getRequestToken()
    }

    override suspend fun getUsername(): String? {
        return factory.retrieveCacheDataStore().getUsername()
    }

    override suspend fun logout() {
        return factory.retrieveDataStore().logout()
    }

    override suspend fun isLoggedIn(): Boolean {
        return factory.retrieveDataStore().isLoggedIn()
    }

}
