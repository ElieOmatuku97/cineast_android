package elieomatuku.cineast_android.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account(
    val avatar: Avatar?,
    val id: Int?,
    val iso6391: String?,
    val iso31661: String?,
    val name: String?,
    val includeAdult: Boolean?,
    val username: String?
) : Parcelable
