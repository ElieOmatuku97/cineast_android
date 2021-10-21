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

/**
 * Created by elieomatuku on 2021-08-22
 */

class PersonRepositoryImpl(private val factory: PersonDataStoreFactory) : PersonRepository {
    override suspend fun getPopularPeople(): List<Person> {
        return factory.retrieveDataStore().getPopularPeople().map {
            it.let(PersonEntity::toPerson)
        }
    }

    override suspend fun getMovies(person: Person): List<Movie> {
        return factory.retrieveRemoteDataStore().getMovies(person.let(PersonEntity::fromPerson))
            .map {
                it.let(MovieEntity::toMovie)
            }
    }

    override suspend fun getDetails(person: Person): PersonDetails {
        return factory.retrieveRemoteDataStore().getDetails(person.let(PersonEntity::fromPerson))
            .let(PersonDetailsEntity::toPersonDetails)
    }

    override suspend fun getImages(person: Person): Images {
        return factory.retrieveRemoteDataStore().getImages(person.let(PersonEntity::fromPerson))
            .let(ImageEntities::toImages)
    }

    override suspend fun searchPeople(argQuery: String): List<Person> {
        return factory.retrieveRemoteDataStore().searchPeople(argQuery).map {
            it.let(PersonEntity::toPerson)
        }
    }
}
