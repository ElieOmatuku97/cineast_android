package elieomatuku.cineast_android.data.source.person

import elieomatuku.cineast_android.data.model.ImageEntities
import elieomatuku.cineast_android.data.model.MovieEntity
import elieomatuku.cineast_android.data.model.PersonDetailsEntity
import elieomatuku.cineast_android.data.model.PersonEntity
import elieomatuku.cineast_android.data.repository.person.PersonCache
import elieomatuku.cineast_android.data.repository.person.PersonDataStore
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class PersonCacheDataStore @Inject constructor(private val personCache: PersonCache) :
    PersonDataStore {
    override suspend fun getPopularPeople(): List<PersonEntity> {
        return personCache.getPopularPeople()
    }

    override suspend fun getMovies(personId: Int): List<MovieEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getDetails(personId: Int): PersonDetailsEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getImages(personId: Int): ImageEntities {
        TODO("Not yet implemented")
    }

    override suspend fun searchPeople(argQuery: String): List<PersonEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePersonality(person: PersonEntity) {
        personCache.updatePerson(person)
    }

    override suspend fun insertPeople(people: List<PersonEntity>) {
        personCache.insertPeople(people)
    }

    override suspend fun insertPerson(person: PersonEntity) {
        personCache.insertPerson(person)
    }

    override suspend fun deletePerson(person: PersonEntity) {
        personCache.deletePerson(person)
    }

    override suspend fun deleteAllPeople() {
        personCache.deleteAllPeople()
    }
}
