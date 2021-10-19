package elieomatuku.cineast_android.data.repository.authentication

import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.AccountEntity
import elieomatuku.cineast_android.data.model.SessionEntity

/**
 * Created by elieomatuku on 2021-08-22
 */

interface AuthenticationDataStore {
    suspend fun getAccessToken(): AccessTokenEntity

    suspend fun setAccessToken(accessTokenEntity: AccessTokenEntity)

    suspend fun setSession(sessionEntity: SessionEntity)

    suspend fun getSession(requestToken: String): SessionEntity

    suspend fun setAccount(accountEntity: AccountEntity)

    suspend fun getAccount(sessionId: String?): AccountEntity

    suspend fun getAccount(): AccountEntity?

    suspend fun logout()

    suspend fun isLoggedIn(): Boolean
}
