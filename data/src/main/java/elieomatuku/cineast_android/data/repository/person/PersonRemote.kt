package elieomatuku.cineast_android.data.repository.person

import elieomatuku.cineast_android.data.model.*


/**
 * Created by elieomatuku on 2021-07-04
 */

interface PersonRemote {
    suspend fun getPopularPeople(): List<PersonEntity>

    suspend fun getPersonMovies(personId: Int): List<MovieEntity>

    suspend fun getPersonDetails(personId: Int): PersonDetailsEntity

    suspend fun getPersonImages(personId: Int): ImageEntities

    suspend fun searchPerson(query: String): List<PersonEntity>
}