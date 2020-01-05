package elieomatuku.cineast_android.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PersonalityDetails(
        val birthday: String? = null,
        val known_for_department: String? = null,
        val deathday: String? = null,
        val id: Int? = null,
        val name: String? = null,
        val also_known_as: Array<String>? = null,
        val gender: Int? = null,
        val biography: String? = null,
        val popularity: Number? = null,
        val place_of_birth: String? = null,
        val profile_path: String? = null,
        val adult: Boolean? = null,
        val imdb_id: String? = null,
        val homepage: String? = null) : Parcelable {


    fun isEmpty(): Boolean {
        val emptyPersonalityDetails = PersonalityDetails()

        return this == emptyPersonalityDetails
    }
}
