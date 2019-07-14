package elieomatuku.cineast_android.business.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateListResponse (val status_code: Int?, val status_message: String?) : Parcelable