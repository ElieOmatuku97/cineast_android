package elieomatuku.cineast_android.data.source.person

import elieomatuku.cineast_android.data.model.ImageEntities
import elieomatuku.cineast_android.data.model.MovieEntity
import elieomatuku.cineast_android.data.model.PersonDetailsEntity
import elieomatuku.cineast_android.data.model.PersonEntity
import elieomatuku.cineast_android.data.repository.person.PersonDataStore
import elieomatuku.cineast_android.data.repository.person.PersonRemote



/**
 * Created by elieomatuku on 2021-08-22
 */

class PersonRemoteDataStore(private val personRemote: PersonRemote): PersonDataStore {
    override suspend fun getPopularPeople(): List<PersonEntity> {
        return personRemote.getPopularPeople()
    }

    override suspend fun getMovies(person: PersonEntity): List<MovieEntity> {
        return personRemote.getPersonMovies(person.id)
    }

    override suspend fun getDetails(person: PersonEntity): PersonDetailsEntity {
        return personRemote.getPersonDetails(person.id)
    }

    override suspend fun getImages(person: PersonEntity): ImageEntities {
        return personRemote.getPersonImages(person.id)
    }

    override suspend fun searchPeople(argQuery: String): List<PersonEntity> {
        return personRemote.searchPerson(argQuery)
    }

    override suspend fun updatePersonality(person: PersonEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun insertPeople(people: List<PersonEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertPerson(person: PersonEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePerson(person: PersonEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllPeople() {
        TODO("Not yet implemented")
    }
}