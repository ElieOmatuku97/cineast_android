package elieomatuku.cineast_android.data.repository.authentication

import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.AccountEntity


/**
 * Created by elieomatuku on 2021-08-22
 */

interface AuthenticationDataStore {
    suspend fun getAccessToken(): AccessTokenEntity

    suspend fun getSession(requestToken: String): Pair<String, AccountEntity>

    suspend fun setAccount(sessionId: String?): Pair<String, AccountEntity>

    suspend fun getRequestToken(): String?

    suspend fun getUsername(): String?

    suspend fun logout()

    suspend fun isLoggedIn(): Boolean
}