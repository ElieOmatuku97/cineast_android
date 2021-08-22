package elieomatuku.cineast_android.data

import elieomatuku.cineast_android.data.source.authentication.AuthenticationDataStoreFactory
import elieomatuku.cineast_android.domain.model.AccessToken
import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository


/**
 * Created by elieomatuku on 2021-08-22
 */

class AuthenticationRepositoryImpl(private val factory: AuthenticationDataStoreFactory): AuthenticationRepository {
    override suspend fun getAccessToken(): AccessToken {
        TODO("Not yet implemented")
    }

    override suspend fun getSession(requestToken: String): Pair<String, Account> {
        TODO("Not yet implemented")
    }

    override suspend fun setAccount(sessionId: String?): Pair<String, Account> {
        TODO("Not yet implemented")
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