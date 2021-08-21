package elieomatuku.cineast_android.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Trailer(
    val id: String?,
    val iso6391: String?,
    val iso31661: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: Int?,
    val type: String?
) : Parcelable
