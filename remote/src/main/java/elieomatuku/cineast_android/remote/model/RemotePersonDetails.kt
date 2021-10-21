package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.PersonDetailsEntity

/**
 * Created by elieomatuku on 2021-07-04
 */

@Keep
data class RemotePersonDetails(
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
    val homepage: String? = null
) {
    companion object {
        fun toPersonDetailsEntity(remotePersonDetails: RemotePersonDetails): PersonDetailsEntity {
            return PersonDetailsEntity(
                remotePersonDetails.birthday,
                remotePersonDetails.known_for_department,
                remotePersonDetails.deathday,
                remotePersonDetails.id,
                remotePersonDetails.name,
                remotePersonDetails.also_known_as?.toList(),
                remotePersonDetails.gender,
                remotePersonDetails.biography,
                remotePersonDetails.popularity,
                remotePersonDetails.place_of_birth,
                remotePersonDetails.profile_path,
                remotePersonDetails.adult,
                remotePersonDetails.imdb_id,
                remotePersonDetails.homepage
            )
        }
    }
}
