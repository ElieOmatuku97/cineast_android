package elieomatuku.cineast_android.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KnownFor(val release_date: String?,
                    val title: String?,
                    val original_title: String?,
                    val id: Int?,
                    val backdrop_path: String?,
                    val poster_path: String? ): Parcelable