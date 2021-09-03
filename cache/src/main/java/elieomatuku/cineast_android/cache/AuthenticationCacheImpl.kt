package elieomatuku.cineast_android.cache

import elieomatuku.cineast_android.data.PrefManager
import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationCache

/**
 * Created by elieomatuku on 2021-09-01
 */

class AuthenticationCacheImpl(private val prefManager: PrefManager) : AuthenticationCache {
    companion object {
        const val REQUEST_TOKEN_KEY = "request_token_key"
        const val SESSION_ID_KEY = "session_id_key"
        const val ACCOUNT_ID_KEY = "account_id_key"
        const val ACCOUNT_USERNAME = "account_username_key"
    }

    override suspend fun getAccessToken(): AccessTokenEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getRequestToken(): String? {
        return prefManager.get(REQUEST_TOKEN_KEY, null)
    }

    override suspend fun getUsername(): String? {
        return prefManager.get(ACCOUNT_USERNAME, null)
    }

    override suspend fun logout() {
        prefManager.remove(SESSION_ID_KEY)
        prefManager.remove(REQUEST_TOKEN_KEY)
        prefManager.remove(ACCOUNT_ID_KEY)
        prefManager.remove(ACCOUNT_USERNAME)
    }

    override fun isLoggedIn(): Boolean {
        return !prefManager.get(SESSION_ID_KEY, null).isNullOrEmpty()
    }
}
