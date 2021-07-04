package elieomatuku.cineast_android.remote

import elieomatuku.cineast_android.remote.api.PersonApi
import elieomatuku.cineast_android.remote.model.RemoteException
import elieomatuku.cineast_android.remote.model.RemoteImages
import elieomatuku.cineast_android.remote.model.RemotePeople
import elieomatuku.cineast_android.remote.model.RemotePerson
import elieomatuku.cineast_android.remote.model.RemotePersonDetails
import elieomatuku.cineast_android.remote.model.RemotePersonMovieCredits

/**
 * Created by elieomatuku on 2021-07-04
 */

class PersonRemoteImpl(private val personApi: PersonApi) {

    suspend fun getPopularPeople(): RemotePeople {
        val response = personApi.getPopularPeople()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getPeopleMovies(person: RemotePerson): RemotePersonMovieCredits {
        val response = personApi.getPersonMovieCredits(person.id)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getPersonDetails(person: RemotePerson): RemotePersonDetails {
        val response = personApi.getPersonDetails(person.id)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getPeopleImages(personId: Int): RemoteImages {
        val response = personApi.getPersonImages(personId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun searchPerson(query: String): RemotePeople {
        val response = personApi.searchPerson(query)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }
}
