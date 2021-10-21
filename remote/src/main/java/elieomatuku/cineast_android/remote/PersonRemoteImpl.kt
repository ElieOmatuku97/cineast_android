package elieomatuku.cineast_android.remote

import elieomatuku.cineast_android.data.model.ImageEntities
import elieomatuku.cineast_android.data.model.MovieEntity
import elieomatuku.cineast_android.data.model.PersonDetailsEntity
import elieomatuku.cineast_android.data.model.PersonEntity
import elieomatuku.cineast_android.data.repository.person.PersonRemote
import elieomatuku.cineast_android.remote.api.PersonApi
import elieomatuku.cineast_android.remote.model.RemoteException
import elieomatuku.cineast_android.remote.model.RemoteImages
import elieomatuku.cineast_android.remote.model.RemoteKnownFor
import elieomatuku.cineast_android.remote.model.RemotePeople
import elieomatuku.cineast_android.remote.model.RemotePersonDetails

/**
 * Created by elieomatuku on 2021-07-04
 */

class PersonRemoteImpl(private val personApi: PersonApi) : PersonRemote {

    override suspend fun getPopularPeople(): List<PersonEntity> {
        val response = personApi.getPopularPeople()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!.let(RemotePeople::toPeopleEntity)
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getPersonMovies(personId: Int): List<MovieEntity> {
        val response = personApi.getPersonMovieCredits(personId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!.cast.map { it.let(RemoteKnownFor::toMovieEntity) }
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getPersonDetails(personId: Int): PersonDetailsEntity {
        val response = personApi.getPersonDetails(personId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!.let(RemotePersonDetails::toPersonDetailsEntity)
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun getPersonImages(personId: Int): ImageEntities {
        val response = personApi.getPersonImages(personId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!.let(RemoteImages::toImageEntities)
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    override suspend fun searchPerson(query: String): List<PersonEntity> {
        val response = personApi.searchPerson(query)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!.let(RemotePeople::toPeopleEntity)
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }
}
