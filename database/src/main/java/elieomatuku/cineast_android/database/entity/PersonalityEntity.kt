package elieomatuku.cineast_android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import elieomatuku.cineast_android.core.model.Personality


/**
 * Created by elieomatuku on 2019-12-20
 */

@Entity(tableName = PersonalityEntity.PERSONALITY_TABLE)
data class PersonalityEntity(
        @PrimaryKey val id: Int,
        val profile_path: String?,
        val adult: Boolean?,
        val name: String?) {

    companion object {
        const val PERSONALITY_TABLE = "personality_table"

        fun fromPersonalities(personalities: List<Personality>): List<PersonalityEntity> {
            return personalities.map { fromPersonality(it) }
        }

        fun fromPersonality(personality: Personality): PersonalityEntity {
            return PersonalityEntity(
                    id = personality.id,
                    profile_path = personality.profile_path,
                    adult = personality.adult,
                    name = personality.name)
        }

        fun toPersonalities(personalityEntities: List<PersonalityEntity>): List<Personality> {
            return personalityEntities.map { it.toPersonality() }
        }
    }

    fun toPersonality(): Personality {
        return Personality(
                id = id,
                profile_path = profile_path,
                adult = adult,
                name = name
        )
    }
}