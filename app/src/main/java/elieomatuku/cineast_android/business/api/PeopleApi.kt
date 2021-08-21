package elieomatuku.cineast_android.business.api

import elieomatuku.cineast_android.business.api.response.ImageResponse
import elieomatuku.cineast_android.business.api.response.PeopleCreditsResponse
import elieomatuku.cineast_android.business.api.response.PersonalityResponse
import elieomatuku.cineast_android.domain.model.PersonDetails
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleApi {
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
    fun getPersonalities(): Deferred<PersonalityResponse>

    @GET(GET_PERSON_ID)
    fun getPeopleDetails(@Path(PERSON_ID) personId: Int): Call <PersonDetails>

    @GET(PERSON_MOVIE_CREDITS)
    fun getPeopleCredits(@Path(PERSON_ID) personId: Int): Call <PeopleCreditsResponse>

    @GET(GET_PERSON_IMAGES)
    fun getPeopleImages(@Path(PERSON_ID) movie_id: Int): Call <ImageResponse>

    @GET(SEARCH_PERSON)
    fun getPeopleWithSearch(@Query(QUERY) query: String): Call <PersonalityResponse>
}
