package elieomatuku.cineast_android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import elieomatuku.cineast_android.domain.model.Person

/**
 * Created by elieomatuku on 2019-12-20
 */

@Entity(tableName = PersonalityEntity.PERSONALITY_TABLE)
data class PersonalityEntity(
    @PrimaryKey val id: Int,
    val profile_path: String?,
    val name: String?
) {

    companion object {
        const val PERSONALITY_TABLE = "personality_table"

        fun fromPersonalities(people: List<Person>): List<PersonalityEntity> {
            return people.map { fromPersonality(it) }
        }

        fun fromPersonality(person: Person): PersonalityEntity {
            return PersonalityEntity(
                id = person.id,
                profile_path = person.profilePath,
                name = person.name
            )
        }

        fun toPersonalities(personalityEntities: List<PersonalityEntity>): List<Person> {
            return personalityEntities.map { it.toPersonality() }
        }
    }

    fun toPersonality(): Person {
        return Person(
            id = id,
            profilePath = profile_path,
            name = name
        )
    }
}
