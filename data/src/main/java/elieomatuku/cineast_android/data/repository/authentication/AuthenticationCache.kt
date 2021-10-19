package elieomatuku.cineast_android.data.repository.authentication

import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.AccountEntity
import elieomatuku.cineast_android.data.model.SessionEntity

/**
 * Created by elieomatuku on 2021-08-21
 */

interface AuthenticationCache {

    suspend fun getAccessToken(): AccessTokenEntity

    suspend fun setAccessToken(accessTokenEntity: AccessTokenEntity)

    suspend fun getSession(): SessionEntity

    suspend fun setSession(sessionEntity: SessionEntity)

    suspend fun setAccount(accountEntity: AccountEntity)

    suspend fun getAccount(): AccountEntity?

    suspend fun logout()

    fun isLoggedIn(): Boolean
}
