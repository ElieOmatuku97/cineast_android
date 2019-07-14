package elieomatuku.cineast_android.business.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddWatchListResponse (val status_code: Int?, val status_message: String?) : Parcelable