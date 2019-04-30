package elieomatuku.restapipractice.business.business.rest

import elieomatuku.restapipractice.business.business.model.data.AccessToken
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthenticationApi {
    @GET("authentication/token/new")
    fun getAccessToken (@Query("api_key") apyKey: String): Call<AccessToken>
}