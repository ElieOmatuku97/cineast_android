package elieomatuku.cineast_android.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Session(val success: Boolean = false, val session_id: String? = null) : Parcelable
