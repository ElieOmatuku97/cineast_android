package elieomatuku.cineast_android.remote.api.model

import androidx.annotation.Keep

/**
 * Created by elieomatuku on 2021-07-04
 */

@Keep
data class RemoteSession(val success: Boolean = false, val session_id: String? = null)
