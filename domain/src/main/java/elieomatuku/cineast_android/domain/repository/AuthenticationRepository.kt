package elieomatuku.cineast_android.domain.repository

import elieomatuku.cineast_android.domain.model.AccessToken
import elieomatuku.cineast_android.domain.model.Account


/**
 * Created by elieomatuku on 2021-08-21
 */

interface AuthenticationRepository {

    suspend fun getAccessToken(): AccessToken

    suspend fun getSession(requestToken: String): Pair<String, Account>

    suspend fun setAccount(sessionId: String?): Pair<String, Account>

    suspend fun getRequestToken(): String?

    suspend fun getUsername(): String?

    suspend fun logout()

    suspend fun isLoggedIn(): Boolean
}