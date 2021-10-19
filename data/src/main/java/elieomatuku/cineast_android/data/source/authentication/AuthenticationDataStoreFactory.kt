package elieomatuku.cineast_android.data.source.authentication

import elieomatuku.cineast_android.data.repository.authentication.AuthenticationCache
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationDataStore

/**
 * Created by elieomatuku on 2021-08-22
 */

class AuthenticationDataStoreFactory(
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
