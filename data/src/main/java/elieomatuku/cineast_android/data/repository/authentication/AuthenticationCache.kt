package elieomatuku.cineast_android.data.repository.authentication

import elieomatuku.cineast_android.data.model.AccessTokenEntity

/**
 * Created by elieomatuku on 2021-08-21
 */

interface AuthenticationCache {
    suspend fun getAccessToken(): AccessTokenEntity

    suspend fun getRequestToken(): String?

    suspend fun getUsername(): String?

    suspend fun logout()

    suspend fun isLoggedIn(): Boolean

    fun isCached(): Boolean

    fun isExpired(): Boolean
}
