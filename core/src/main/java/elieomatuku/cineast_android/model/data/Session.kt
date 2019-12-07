package elieomatuku.cineast_android.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Session (val success: Boolean =  false, val session_id:  String? = null ) : Parcelable

