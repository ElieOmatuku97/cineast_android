package elieomatuku.cineast_android.business.business.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PeopleDetails ( val birthday: String?,
                           val known_for_department: String?,
                           val deathday: String?,
                           val id: Int?, val name: String?,
                           val also_known_as: Array <String>?,
                           val gender: Int?,
                           val biography: String?,
                           val popularity: Number?,
                           val place_of_birth: String?,
                           val profile_path: String?,
                           val adult: Boolean?,
                           val imdb_id: String?,
                           val homepage: String?): Parcelable
