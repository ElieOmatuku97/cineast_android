package elieomatuku.cineast_android.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Session(val success: Boolean = false, val sessionId: String? = null) : Parcelable
