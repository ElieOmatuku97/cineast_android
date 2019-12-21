package elieomatuku.cineast_android.business.api

import elieomatuku.cineast_android.model.data.PeopleDetails
import elieomatuku.cineast_android.business.api.response.ImageResponse
import elieomatuku.cineast_android.business.api.response.PeopleCreditsResponse
import elieomatuku.cineast_android.business.api.response.PeopleResponse
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleApi {
    companion object {

        const val API_KEY = "api_key"
        const val POPULAR_PERSON = "person/popular"
        const val QUERY = "query"
        const val PERSON_ID = "person_id"

        const val GET_PERSON_ID = "person/{person_id}"
        const val GET_PERSON_IMAGES = "person/{person_id}/images"
        const val SEARCH_PERSON = "search/person"
        const val PERSON_MOVIE_CREDITS = "person/{person_id}/movie_credits"
    }

    @GET (POPULAR_PERSON )
    fun getPopularPeople (@Query( API_KEY) apiKey: String): Deferred<PeopleResponse>

    @GET (GET_PERSON_ID )
    fun getPeopleDetails (@Path(PERSON_ID) personId: Int, @Query( API_KEY) apiKey: String): Call <PeopleDetails>

    @GET (PERSON_MOVIE_CREDITS)
    fun getPeopleCredits (@Path(PERSON_ID) personId: Int, @Query ( API_KEY) apiKey: String): Call <PeopleCreditsResponse>


    @GET(GET_PERSON_IMAGES)
    fun getPeopleImages(@Path(PERSON_ID) movie_id: Int, @Query( API_KEY) apiKey: String): Call <ImageResponse>


    @GET (SEARCH_PERSON)
    fun getPeopleWithSearch(@Query( API_KEY) apiKey: String, @Query(QUERY) query: String): Call <PeopleResponse>
}