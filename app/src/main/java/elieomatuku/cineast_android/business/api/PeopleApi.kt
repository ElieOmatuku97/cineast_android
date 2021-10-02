package elieomatuku.cineast_android.business.api


import elieomatuku.cineast_android.business.api.response.PersonalityResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PeopleApi {
    companion object {
        const val QUERY = "query"
        const val SEARCH_PERSON = "search/person"
    }


    @GET(SEARCH_PERSON)
    fun getPeopleWithSearch(@Query(QUERY) query: String): Call<PersonalityResponse>
}
