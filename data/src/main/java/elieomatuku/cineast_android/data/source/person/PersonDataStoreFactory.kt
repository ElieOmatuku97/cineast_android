package elieomatuku.cineast_android.data.source.person

import elieomatuku.cineast_android.data.repository.person.PersonCache
import elieomatuku.cineast_android.data.repository.person.PersonDataStore

/**
 * Created by elieomatuku on 2021-08-22
 */

class PersonDataStoreFactory(
    private val personCache: PersonCache,
    private val personCacheDataStore: PersonCacheDataStore,
    private val personRemoteDataStore: PersonRemoteDataStore
) {

    fun retrieveDataStore(): PersonDataStore {
        return if (personCache.isCached() && !personCache.isExpired()) {
            retrieveCacheDataStore()
        } else {
            retrieveRemoteDataStore()
        }
    }

    fun retrieveCacheDataStore(): PersonDataStore {
        return personCacheDataStore
    }

    fun retrieveRemoteDataStore(): PersonDataStore {
        return personRemoteDataStore
    }

    fun isCached(): Boolean {
        return personCache.isCached()
    }
}
