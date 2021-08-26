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
        return if (authenticationCache.isCached() && !authenticationCache.isExpired()) {
            retrieveCacheDataStore()
        } else {
            retrieveRemoteDataStore()
        }
    }

    fun retrieveCacheDataStore(): AuthenticationDataStore {
        return authenticationRemoteDataStore
    }

    fun retrieveRemoteDataStore(): AuthenticationDataStore {
        return authenticationCacheDataStore
    }

    fun isCached(): Boolean {
        return authenticationCache.isCached()
    }
}