package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.SessionEntity

/**
 * Created by elieomatuku on 2021-07-04
 */

@Keep
data class RemoteSession(val success: Boolean = false, val session_id: String? = null) {
    companion object {
        fun toSessionEntity(remoteSession: RemoteSession): SessionEntity {
            return SessionEntity(
                remoteSession.success,
                remoteSession.session_id
            )
        }
    }
}
