package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.PostResultEntity

@Keep
data class RemotePostResult(val status_code: Int?, val status_message: String?) {
    companion object {
        fun toPostResultEntity(remotePostResult: RemotePostResult): PostResultEntity {
            return PostResultEntity(
                remotePostResult.status_code,
                remotePostResult.status_message
            )
        }
    }
}
