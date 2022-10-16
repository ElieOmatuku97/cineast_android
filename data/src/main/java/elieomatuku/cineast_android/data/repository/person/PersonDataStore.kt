package elieomatuku.cineast_android.data.repository.person

import elieomatuku.cineast_android.data.model.ImageEntities
import elieomatuku.cineast_android.data.model.MovieEntity
import elieomatuku.cineast_android.data.model.PersonDetailsEntity
import elieomatuku.cineast_android.data.model.PersonEntity

/**
 * Created by elieomatuku on 2021-08-22
 */

interface PersonDataStore {
    suspend fun getPopularPeople(): List<PersonEntity>

    suspend fun getMovies(personId: Int): List<MovieEntity>

    suspend fun getDetails(personId: Int): PersonDetailsEntity

    suspend fun getImages(person: PersonEntity): ImageEntities

    suspend fun searchPeople(argQuery: String): List<PersonEntity>

    suspend fun updatePersonality(person: PersonEntity)

    suspend fun insertPeople(people: List<PersonEntity>)

    suspend fun insertPerson(person: PersonEntity)

    suspend fun deletePerson(person: PersonEntity)

    suspend fun deleteAllPeople()
}
