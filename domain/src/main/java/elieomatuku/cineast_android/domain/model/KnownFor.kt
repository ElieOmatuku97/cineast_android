package elieomatuku.cineast_android.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KnownFor(
    val release_date: String?,
    val title: String?,
    val original_title: String?,
    val id: Int?,
    val backdrop_path: String?,
    val poster_path: String?
) : Parcelable {

    fun toMovie(): Movie? {
        return id?.let { Movie(id = id, poster_path = poster_path, release_date = release_date, original_title = original_title) }
    }
}
