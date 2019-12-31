package elieomatuku.cineast_android.business.api

import elieomatuku.cineast_android.model.data.AccessToken
import elieomatuku.cineast_android.model.data.Session
import elieomatuku.cineast_android.model.data.Account
import retrofit2.Call
import retrofit2.http.*

interface AuthenticationApi {
    @GET("authentication/token/new")
    fun getAccessToken (@Query("api_key") apyKey: String): Call<AccessToken>


    @FormUrlEncoded
    @POST("authentication/session/new")
    fun getSession(@Query("api_key") apyKey: String, @Field("request_token")  requestToken: String?): Call<Session>


    @GET("account")
    fun getAccount(@Query("api_key") apyKey: String, @Query("session_id") sessionId: String): Call<Account>
}