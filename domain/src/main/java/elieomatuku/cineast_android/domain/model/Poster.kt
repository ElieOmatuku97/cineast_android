package elieomatuku.cineast_android.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Poster(
    val aspectRatio: Number?,
    val filePath: String?,
    val height: Int?,
    val iso6391: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val width: Int?
) : Parcelable
