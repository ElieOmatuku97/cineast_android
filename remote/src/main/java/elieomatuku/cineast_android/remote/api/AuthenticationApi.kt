package elieomatuku.cineast_android.remote.api

import elieomatuku.cineast_android.remote.model.RemoteAccessToken
import elieomatuku.cineast_android.remote.model.RemoteAccount
import elieomatuku.cineast_android.remote.model.RemoteSession
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {
    @GET("authentication/token/new")
    suspend fun getAccessToken(): Response<RemoteAccessToken>

    @FormUrlEncoded
    @POST("authentication/session/new")
    suspend fun getSession(@Field("request_token") requestToken: String?): Response<RemoteSession>

    @GET("account")
    suspend fun getAccount(@Query("session_id") sessionId: String?): Response<RemoteAccount>
}
