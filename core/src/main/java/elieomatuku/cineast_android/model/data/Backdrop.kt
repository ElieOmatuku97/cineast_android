package elieomatuku.cineast_android.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Backdrop (val aspect_ratio: Number?,
                     val file_path: String?,
                     val height: Int?,
                     val iso_639_1: String?,
                     val vote_average: Double?,
                     val vote_count: Int?,
                     val width: Int?): Parcelable
