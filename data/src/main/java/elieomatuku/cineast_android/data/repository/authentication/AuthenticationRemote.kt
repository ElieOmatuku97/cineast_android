package elieomatuku.cineast_android.data.repository.authentication

import elieomatuku.cineast_android.data.model.AccessTokenEntity
import elieomatuku.cineast_android.data.model.AccountEntity
import elieomatuku.cineast_android.data.model.SessionEntity

/**
 * Created by elieomatuku on 2021-07-04
 */

interface AuthenticationRemote {
    suspend fun getAccessToken(): AccessTokenEntity

    suspend fun getSession(requestToken: String?): SessionEntity

    suspend fun getAccount(sessionId: String?): AccountEntity
}
