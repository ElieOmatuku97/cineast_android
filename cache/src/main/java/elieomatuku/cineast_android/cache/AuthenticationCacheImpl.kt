package elieomatuku.cineast_android.cache

import elieomatuku.cineast_android.data.MoshiSerializer
import elieomatuku.cineast_android.data.PrefManager
import elieomatuku.cineast_android.data.Serializer
import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.AccountEntity
import elieomatuku.cineast_android.data.model.SessionEntity
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationCache
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-09-01
 */

class AuthenticationCacheImpl @Inject constructor (private val prefManager: PrefManager) : AuthenticationCache {
    companion object {
        const val SESSION_KEY = "session_key"
        const val ACCOUNT_KEY = "account_key"
        const val ACCESS_TOKEN_KEY = "access_token_key"
    }

    private val accessTokenSerializer: Serializer<AccessTokenEntity> by lazy {
        MoshiSerializer<AccessTokenEntity>(AccessTokenEntity::class.java)
    }

    private val sessionSerializer: Serializer<SessionEntity> by lazy {
        MoshiSerializer<SessionEntity>(SessionEntity::class.java)
    }

    private val accountSerializer: Serializer<AccountEntity> by lazy {
        MoshiSerializer<AccountEntity>(AccountEntity::class.java)
    }

    override suspend fun getAccessToken(): AccessTokenEntity {
        return prefManager.get(ACCESS_TOKEN_KEY, null)?.let {
            accessTokenSerializer.fromJson(it)
        } ?: AccessTokenEntity()
    }

    override suspend fun setAccessToken(accessTokenEntity: AccessTokenEntity) {
        prefManager.set(ACCESS_TOKEN_KEY, accessTokenSerializer.toJson(accessTokenEntity))
    }

    override suspend fun getSession(): SessionEntity {
        return prefManager.get(SESSION_KEY, null)?.let {
            sessionSerializer.fromJson(it)
        } ?: SessionEntity()
    }

    override suspend fun setSession(sessionEntity: SessionEntity) {
        prefManager.set(SESSION_KEY, sessionSerializer.toJson(sessionEntity))
    }

    override suspend fun setAccount(accountEntity: AccountEntity) {
        prefManager.set(ACCOUNT_KEY, accountSerializer.toJson(accountEntity))
    }

    override suspend fun getAccount(): AccountEntity? {
        return prefManager.get(ACCOUNT_KEY, null)?.let {
            accountSerializer.fromJson(it)
        }
    }

    override suspend fun logout() {
        prefManager.remove(ACCESS_TOKEN_KEY)
        prefManager.remove(SESSION_KEY)
        prefManager.remove(ACCOUNT_KEY)
    }

    override fun isLoggedIn(): Boolean {
        val session = prefManager.get(SESSION_KEY, null)?.let {
            sessionSerializer.fromJson(it)
        }
        return session != null
    }
}
