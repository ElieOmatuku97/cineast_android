package elieomatuku.cineast_android.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import elieomatuku.cineast_android.data.model.PersonEntity

/**
 * Created by elieomatuku on 2019-12-20
 */

@Entity(tableName = CachePerson.PERSONALITY_TABLE)
data class CachePerson(
    @PrimaryKey val id: Int,
    val profile_path: String?,
    val name: String?
) {

    companion object {
        const val PERSONALITY_TABLE = "personality_table"

        fun fromPersonEntity(person: PersonEntity): CachePerson {
            return CachePerson(
                id = person.id,
                profile_path = person.profilePath,
                name = person.name
            )
        }

        fun toPeople(cachePeople: List<CachePerson>): List<PersonEntity> {
            return cachePeople.map { it.toPersonality() }
        }
    }

    fun toPersonality(): PersonEntity {
        return PersonEntity(
            id = id,
            profilePath = profile_path,
            name = name
        )
    }
}
