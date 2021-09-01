package elieomatuku.cineast_android.domain.repository

import elieomatuku.cineast_android.domain.model.AccessToken
import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.domain.model.Session


/**
 * Created by elieomatuku on 2021-08-21
 */

interface AuthenticationRepository {

    suspend fun getAccessToken(): AccessToken

    suspend fun getSession(requestToken: String): Session

    suspend fun setAccount(sessionId: String?): Account

    suspend fun getRequestToken(): String?

    suspend fun getUsername(): String?

    suspend fun logout()

    suspend fun isLoggedIn(): Boolean
}