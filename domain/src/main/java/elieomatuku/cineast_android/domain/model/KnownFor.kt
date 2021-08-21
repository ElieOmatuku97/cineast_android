package elieomatuku.cineast_android.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KnownFor(
    val release_date: String?,
    val title: String?,
    val originalTitle: String?,
    val id: Int?,
    val backdropPath: String?,
    val posterPath: String?
) : Parcelable {

    fun toMovie(): Movie? {
        return id?.let { Movie(id = id, poster_path = posterPath, releaseDate = release_date, originalTitle = originalTitle) }
    }
}
