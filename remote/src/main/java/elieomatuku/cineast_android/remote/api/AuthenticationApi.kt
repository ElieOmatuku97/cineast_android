package elieomatuku.cineast_android.remote.api

import elieomatuku.cineast_android.remote.api.model.RemoteAccessToken
import elieomatuku.cineast_android.remote.api.model.RemoteAccount
import elieomatuku.cineast_android.remote.api.model.RemoteSession
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {
    @GET("authentication/token/new")
    fun getAccessToken(): Call<RemoteAccessToken>

    @FormUrlEncoded
    @POST("authentication/session/new")
    fun getSession(@Field("request_token") requestToken: String?): Call<RemoteSession>

    @GET("account")
    fun getAccount(@Query("session_id") sessionId: String): Call<RemoteAccount>
}
