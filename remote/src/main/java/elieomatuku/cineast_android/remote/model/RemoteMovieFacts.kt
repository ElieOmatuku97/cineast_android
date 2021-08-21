package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.MovieFactsEntity

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
) {
    companion object {
        fun toMovieFactsEntity(remoteMovieFacts: RemoteMovieFacts): MovieFactsEntity {
            return MovieFactsEntity(
                remoteMovieFacts.budget,
                remoteMovieFacts.release_date,
                remoteMovieFacts.runtime,
                remoteMovieFacts.revenue,
                remoteMovieFacts.homepage
            )
        }
    }
}
