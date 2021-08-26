package elieomatuku.cineast_android.domain.repository

import elieomatuku.cineast_android.domain.model.Images
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.domain.model.PersonDetails


/**
 * Created by elieomatuku on 2021-08-21
 */

interface PersonRepository {
    suspend fun getPopularPeople(): List<Person>

    suspend fun getMovies(person: Person): List<Movie>

    suspend fun getDetails(person: Person): PersonDetails

    suspend fun getImages(person: Person): Images

    suspend fun searchPeople(argQuery: String): List<Person>
}