package elieomatuku.cineast_android.data.source.movie

import elieomatuku.cineast_android.data.repository.movie.MovieCache
import elieomatuku.cineast_android.data.repository.movie.MovieDataStore
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class MovieDataStoreFactory @Inject constructor(
    private val movieCache: MovieCache,
    private val movieCacheDataStore: MovieCacheDataStore,
    private val movieRemoteDataStore: MovieRemoteDataStore
) {

    fun retrieveDataStore(): MovieDataStore {
        return if (movieCache.isCached() && !movieCache.isExpired()) {
            retrieveCacheDataStore()
        } else {
            retrieveRemoteDataStore()
        }
    }

    fun retrieveCacheDataStore(): MovieDataStore {
        return movieCacheDataStore
    }

    fun retrieveRemoteDataStore(): MovieDataStore {
        return movieRemoteDataStore
    }

    fun isCached(): Boolean {
        return movieCache.isCached()
    }
}
