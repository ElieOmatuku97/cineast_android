package elieomatuku.cineast_android.business.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class AccessToken (val success: Boolean =  false, val expires_at: String? = null, val request_token: String? = null ) : Parcelable