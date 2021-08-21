package elieomatuku.cineast_android.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccessToken(
    val success: Boolean = false,
    val expiresAt: String? = null,
    val requestToken: String? = null
) : Parcelable
