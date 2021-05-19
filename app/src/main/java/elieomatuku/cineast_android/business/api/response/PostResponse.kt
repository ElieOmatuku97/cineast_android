package elieomatuku.cineast_android.business.api.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostResponse(val status_code: Int?, val status_message: String?) : Parcelable
