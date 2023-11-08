package elieomatuku.cineast_android.data.source.person

import elieomatuku.cineast_android.data.model.ImageEntities
import elieomatuku.cineast_android.data.model.MovieEntity
import elieomatuku.cineast_android.data.model.PersonDetailsEntity
import elieomatuku.cineast_android.data.model.PersonEntity
import elieomatuku.cineast_android.data.repository.person.PersonDataStore
import elieomatuku.cineast_android.data.repository.person.PersonRemote
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class PersonRemoteDataStore @Inject constructor(private val personRemote: PersonRemote) :
    PersonDataStore {
    override suspend fun getPopularPeople(): List<PersonEntity> {
        return personRemote.getPopularPeople()
    }

    override suspend fun getMovies(personId: Int): List<MovieEntity> {
        return personRemote.getPersonMovies(personId)
    }

    override suspend fun getDetails(personId: Int): PersonDetailsEntity {
        return personRemote.getPersonDetails(personId)
    }

    override suspend fun getImages(personId: Int): ImageEntities {
        return personRemote.getPersonImages(personId)
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
