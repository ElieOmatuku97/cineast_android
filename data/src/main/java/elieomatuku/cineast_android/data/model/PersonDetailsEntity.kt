package elieomatuku.cineast_android.data.model

import elieomatuku.cineast_android.domain.model.PersonDetails

/**
 * Created by elieomatuku on 2021-07-04
 */

data class PersonDetailsEntity(
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
) {
    companion object {
        fun toPersonDetails(personDetailsEntity: PersonDetailsEntity): PersonDetails {
            return PersonDetails(
                birthday = personDetailsEntity.birthday,
                knownForDepartment = personDetailsEntity.knownForDepartment,
                deathDay = personDetailsEntity.deathDay,
                id = personDetailsEntity.id,
                name = personDetailsEntity.name,
                alsoKnownAs = personDetailsEntity.alsoKnownAs,
                gender = personDetailsEntity.gender,
                biography = personDetailsEntity.biography,
                popularity = personDetailsEntity.popularity,
                placeOfBirth = personDetailsEntity.placeOfBirth,
                profilePath = personDetailsEntity.profilePath,
                adult = personDetailsEntity.adult,
                imdbId = personDetailsEntity.imdbId,
                homepage = personDetailsEntity.homepage
            )
        }

        fun fromPersonDetails(personDetails: PersonDetails): PersonDetailsEntity {
            return PersonDetailsEntity(
                birthday = personDetails.birthday,
                knownForDepartment = personDetails.knownForDepartment,
                deathDay = personDetails.deathDay,
                id = personDetails.id,
                name = personDetails.name,
                alsoKnownAs = personDetails.alsoKnownAs,
                gender = personDetails.gender,
                biography = personDetails.biography,
                popularity = personDetails.popularity,
                placeOfBirth = personDetails.placeOfBirth,
                profilePath = personDetails.profilePath,
                adult = personDetails.adult,
                imdbId = personDetails.imdbId,
                homepage = personDetails.homepage
            )
        }
    }
}
