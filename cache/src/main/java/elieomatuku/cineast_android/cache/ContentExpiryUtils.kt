package elieomatuku.cineast_android.cache

/**
 * Created by elieomatuku on 2021-09-01
 */

object ContentExpiryUtils {
    const val TIMESTAMP = "timestamp"
    private const val STALE_MS = 3600000 // Data is stale after an 1hour

    fun isUpToDate(timeStamp: Long): Boolean {
        return System.currentTimeMillis() - timeStamp < STALE_MS
    }
}
