package elieomatuku.cineast_android.data.model

import elieomatuku.cineast_android.domain.model.Session

/**
 * Created by elieomatuku on 2021-07-04
 */

data class SessionEntity(val success: Boolean = false, val sessionId: String? = null) {
    companion object {
        fun toSession(sessionEntity: SessionEntity): Session {
            return Session(
                sessionEntity.success,
                sessionEntity.sessionId
            )
        }
    }
}
