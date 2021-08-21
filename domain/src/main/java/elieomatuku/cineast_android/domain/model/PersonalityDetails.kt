package elieomatuku.cineast_android.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PersonalityDetails(
    val birthday: String? = null,
    val knownForDepartment: String? = null,
    val deathDay: String? = null,
    val id: Int? = null,
    val name: String? = null,
    val alsoKnownAs: List<String>? = null,
    val gender: Int? = null,
    val biography: String? = null,
    val popularity: Number? = null,
    val placeOfBirth: String? = null,
    val profilePath: String? = null,
    val adult: Boolean? = null,
    val imdbId: String? = null,
    val homepage: String? = null
) : Parcelable {

    fun isEmpty(): Boolean {
        val emptyPersonalityDetails = PersonalityDetails()

        return this == emptyPersonalityDetails
    }
}
