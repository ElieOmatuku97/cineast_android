package elieomatuku.cineast_android.data.repository.person

import elieomatuku.cineast_android.data.model.PersonEntity

/**
 * Created by elieomatuku on 2021-08-21
 */

interface PersonCache {
    fun updatePerson(person: PersonEntity)
    fun insertPeople(people: List<PersonEntity>)
    fun insertPerson(person: PersonEntity)
    fun deletePerson(person: PersonEntity)
    fun deleteAllPeople()
    fun getPopularPeople(): List<PersonEntity>
    fun isCached(): Boolean
    fun isExpired(): Boolean
}
