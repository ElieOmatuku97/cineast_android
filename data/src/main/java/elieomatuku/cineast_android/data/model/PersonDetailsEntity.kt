package elieomatuku.cineast_android.data.model


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
)
