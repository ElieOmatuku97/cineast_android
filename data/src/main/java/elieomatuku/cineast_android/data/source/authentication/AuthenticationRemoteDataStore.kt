package elieomatuku.cineast_android.data.source.authentication

import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.AccountEntity
import elieomatuku.cineast_android.data.model.SessionEntity
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationDataStore
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationRemote
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class AuthenticationRemoteDataStore @Inject constructor (private val authenticationRemote: AuthenticationRemote) :
    AuthenticationDataStore {
    override suspend fun getAccessToken(): AccessTokenEntity {
        return authenticationRemote.getAccessToken()
    }

    override suspend fun setAccessToken(accessTokenEntity: AccessTokenEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun setSession(sessionEntity: SessionEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getSession(requestToken: String): SessionEntity {
        return authenticationRemote.getSession(requestToken)
    }

    override suspend fun setAccount(accountEntity: AccountEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getAccount(sessionId: String?): AccountEntity {
        return authenticationRemote.getAccount(sessionId)
    }

    override suspend fun getAccount(): AccountEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun isLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }
}
