package elieomatuku.cineast_android.data

import elieomatuku.cineast_android.data.source.person.PersonDataStoreFactory
import elieomatuku.cineast_android.domain.model.Images
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.domain.model.PersonDetails
import elieomatuku.cineast_android.domain.repository.PersonRepository


/**
 * Created by elieomatuku on 2021-08-22
 */

class PersonRepositoryImpl(private val factory: PersonDataStoreFactory): PersonRepository {
    override suspend fun personalities(): List<Person> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovies(person: Person): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getDetails(person: Person): PersonDetails {
        TODO("Not yet implemented")
    }

    override suspend fun getImages(person: Person): Images {
        TODO("Not yet implemented")
    }

    override suspend fun searchPeople(argQuery: String): List<Person> {
        TODO("Not yet implemented")
    }
}