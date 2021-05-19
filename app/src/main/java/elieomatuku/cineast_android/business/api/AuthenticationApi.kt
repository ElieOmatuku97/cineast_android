package elieomatuku.cineast_android.business.api

import elieomatuku.cineast_android.core.model.AccessToken
import elieomatuku.cineast_android.core.model.Account
import elieomatuku.cineast_android.core.model.Session
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {
    @GET("authentication/token/new")
    fun getAccessToken(): Call<AccessToken>

    @FormUrlEncoded
    @POST("authentication/session/new")
    fun getSession(@Field("request_token") requestToken: String?): Call<Session>

    @GET("account")
    fun getAccount(@Query("session_id") sessionId: String): Call<Account>
}
