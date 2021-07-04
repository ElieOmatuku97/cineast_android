package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep

/**
 * Created by elieomatuku on 2021-07-04
 */

@Keep
data class RemoteMovieFacts(
    val budget: Int?,
    val release_date: String?,
    val runtime: Int?,
    val revenue: Int?,
    val homepage: String?
)
