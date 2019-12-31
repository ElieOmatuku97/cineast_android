package elieomatuku.cineast_android.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieFacts(
        val budget: Int?,
        val release_date: String?,
        val runtime: Int?,
        val revenue: Int?,
        val homepage: String?) : Parcelable