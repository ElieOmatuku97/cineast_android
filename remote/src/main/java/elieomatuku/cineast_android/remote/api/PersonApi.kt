package elieomatuku.cineast_android.remote.api

import elieomatuku.cineast_android.remote.model.RemoteImages
import elieomatuku.cineast_android.remote.model.RemotePeople
import elieomatuku.cineast_android.remote.model.RemotePersonDetails
import elieomatuku.cineast_android.remote.model.RemotePersonMovieCredits
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PersonApi {
    companion object {
        const val POPULAR_PERSON = "person/popular"
        const val QUERY = "query"
        const val PERSON_ID = "person_id"

        const val GET_PERSON_ID = "person/{person_id}"
        const val GET_PERSON_IMAGES = "person/{person_id}/images"
        const val SEARCH_PERSON = "search/person"
        const val PERSON_MOVIE_CREDITS = "person/{person_id}/movie_credits"
    }

    @GET(POPULAR_PERSON)
    suspend fun getPopularPeople(): Response<RemotePeople>

    @GET(GET_PERSON_ID)
    suspend fun getPersonDetails(@Path(PERSON_ID) personId: Int): Response<RemotePersonDetails>

    @GET(PERSON_MOVIE_CREDITS)
    suspend fun getPersonMovieCredits(@Path(PERSON_ID) personId: Int): Response<RemotePersonMovieCredits>

    @GET(GET_PERSON_IMAGES)
    suspend fun getPersonImages(@Path(PERSON_ID) movie_id: Int): Response<RemoteImages>

    @GET(SEARCH_PERSON)
    suspend fun searchPerson(@Query(QUERY) query: String): Response<RemotePeople>
}
