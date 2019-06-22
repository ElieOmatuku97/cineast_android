package elieomatuku.cineast_android.business.business.rest

import elieomatuku.cineast_android.business.business.model.data.AccessToken
import elieomatuku.cineast_android.business.business.model.data.Session
import retrofit2.Call
import retrofit2.http.*

interface AuthenticationApi {
    @GET("authentication/token/new")
    fun getAccessToken (@Query("api_key") apyKey: String): Call<AccessToken>


    @FormUrlEncoded
    @POST("authentication/session/new")
    fun getSession(@Query("api_key") apyKey: String, @Field("request_token")  requestToken: String?): Call<Session>
}