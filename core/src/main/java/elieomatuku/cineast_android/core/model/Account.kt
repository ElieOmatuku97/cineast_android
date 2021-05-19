package elieomatuku.cineast_android.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account(
    val avatar: Avatar?,
    val id: Int?,
    val iso_639_1: String?,
    val iso_3166_1: String?,
    val name: String?,
    val include_adult: Boolean?,
    val username: String?
) : Parcelable
