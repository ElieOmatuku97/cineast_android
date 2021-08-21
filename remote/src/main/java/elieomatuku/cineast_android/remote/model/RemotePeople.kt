package elieomatuku.cineast_android.remote.model

import androidx.annotation.Keep
import elieomatuku.cineast_android.data.model.PersonEntity

@Keep
data class RemotePeople(
    val results: List<RemotePerson> = listOf()
)

@Keep
data class RemotePerson(
    val id: Int,
    val name: String?,
    val profile_path: String?,
) {
    companion object {
        fun toPersonEntity(remotePerson: RemotePerson): PersonEntity {
            return PersonEntity(
                id = remotePerson.id,
                name = remotePerson.name,
                profilePath = remotePerson.profile_path
            )
        }
    }
}
