package elieomatuku.cineast_android.cache

import elieomatuku.cineast_android.data.MoshiSerializer
import elieomatuku.cineast_android.data.PrefManager
import elieomatuku.cineast_android.data.Serializer
import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.SessionEntity
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationCache

/**
 * Created by elieomatuku on 2021-09-01
 */

class AuthenticationCacheImpl(private val prefManager: PrefManager) : AuthenticationCache {
    companion object {
        const val REQUEST_TOKEN_KEY = "request_token_key"
        const val SESSION_KEY = "session_key"
        const val ACCOUNT_ID_KEY = "account_id_key"
        const val ACCOUNT_USERNAME = "account_username_key"
        const val ACCESS_TOKEN_KEY = "access_token_key"
    }

    private val accessTokenSerializer: Serializer<AccessTokenEntity> by lazy {
        MoshiSerializer<AccessTokenEntity>(AccessTokenEntity::class.java)
    }

    private val sessionSerializer: Serializer<SessionEntity> by lazy {
        MoshiSerializer<SessionEntity>(SessionEntity::class.java)
    }

    override suspend fun getAccessToken(): AccessTokenEntity {
        return prefManager.get(ACCESS_TOKEN_KEY, null)?.let {
            accessTokenSerializer.fromJson(it)
        } ?: AccessTokenEntity()
    }

    override suspend fun setAccessToken(accessTokenEntity: AccessTokenEntity) {
        prefManager.set(ACCESS_TOKEN_KEY, accessTokenSerializer.toJson(accessTokenEntity))
    }

    override suspend fun setSession(sessionEntity: SessionEntity) {
        prefManager.set(SESSION_KEY, sessionSerializer.toJson(sessionEntity))
    }

    override suspend fun getRequestToken(): String? {
        return prefManager.get(REQUEST_TOKEN_KEY, null)
    }

    override suspend fun getUsername(): String? {
        return prefManager.get(ACCOUNT_USERNAME, null)
    }

    override suspend fun logout() {
        prefManager.remove(SESSION_KEY)
        prefManager.remove(REQUEST_TOKEN_KEY)
        prefManager.remove(ACCOUNT_ID_KEY)
        prefManager.remove(ACCOUNT_USERNAME)
    }

    override fun isLoggedIn(): Boolean {
        val session = prefManager.get(SESSION_KEY, null)?.let {
            sessionSerializer.fromJson(it)
        }
        return session != null
    }
}
