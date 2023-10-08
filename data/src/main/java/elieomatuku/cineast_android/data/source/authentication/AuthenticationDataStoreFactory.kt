package elieomatuku.cineast_android.data.source.authentication

import elieomatuku.cineast_android.data.repository.authentication.AuthenticationCache
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationDataStore
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class AuthenticationDataStoreFactory @Inject constructor(
    private val authenticationCache: AuthenticationCache,
    private val authenticationCacheDataStore: AuthenticationCacheDataStore,
    private val authenticationRemoteDataStore: AuthenticationRemoteDataStore
) {

    fun retrieveDataStore(): AuthenticationDataStore {
        return if (authenticationCache.isLoggedIn()) {
            retrieveCacheDataStore()
        } else {
            return retrieveRemoteDataStore()
        }
    }

    fun retrieveCacheDataStore(): AuthenticationDataStore {
        return authenticationCacheDataStore
    }

    fun retrieveRemoteDataStore(): AuthenticationDataStore {
        return authenticationRemoteDataStore
    }
}
