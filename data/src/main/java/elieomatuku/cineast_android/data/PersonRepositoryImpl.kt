package elieomatuku.cineast_android.data

import elieomatuku.cineast_android.data.model.ImageEntities
import elieomatuku.cineast_android.data.model.MovieEntity
import elieomatuku.cineast_android.data.model.PersonDetailsEntity
import elieomatuku.cineast_android.data.model.PersonEntity
import elieomatuku.cineast_android.data.source.person.PersonDataStoreFactory
import elieomatuku.cineast_android.domain.model.Images
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.domain.model.PersonDetails
import elieomatuku.cineast_android.domain.repository.PersonRepository
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class PersonRepositoryImpl @Inject constructor (private val factory: PersonDataStoreFactory) : PersonRepository {
    override suspend fun getPopularPeople(): List<Person> {
        return factory.retrieveDataStore().getPopularPeople().map {
            it.let(PersonEntity::toPerson)
        }
    }

    override suspend fun getMovies(personId: Int): List<Movie> {
        return factory.retrieveRemoteDataStore().getMovies(personId)
            .map {
                it.let(MovieEntity::toMovie)
            }
    }

    override suspend fun getDetails(personId: Int): PersonDetails {
        return factory.retrieveRemoteDataStore().getDetails(personId)
            .let(PersonDetailsEntity::toPersonDetails)
    }

    override suspend fun getImages(personId: Int): Images {
        return factory.retrieveRemoteDataStore().getImages(personId)
            .let(ImageEntities::toImages)
    }

    override suspend fun searchPeople(argQuery: String): List<Person> {
        return factory.retrieveRemoteDataStore().searchPeople(argQuery).map {
            it.let(PersonEntity::toPerson)
        }
    }
}
