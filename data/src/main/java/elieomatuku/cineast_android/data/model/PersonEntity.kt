package elieomatuku.cineast_android.data.model

import elieomatuku.cineast_android.domain.model.Person

data class PersonEntity(
    val profilePath: String?,
    val id: Int,
    val name: String?
) {
    companion object {
        fun toPerson(personEntity: PersonEntity): Person {
            return Person(
                personEntity.profilePath,
                personEntity.id,
                personEntity.name
            )
        }

        fun fromPerson(person: Person): PersonEntity {
            return PersonEntity(
                person.profilePath,
                person.id,
                person.name
            )
        }
    }
}
