package elieomatuku.cineast_android.business.client

import android.app.Application
import android.content.res.Resources
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.model.data.*
import elieomatuku.cineast_android.business.api.AuthenticationApi
import elieomatuku.cineast_android.business.service.PrefsStore
import elieomatuku.cineast_android.utils.ApiUtils
import elieomatuku.cineast_android.utils.RestUtils
import elieomatuku.cineast_android.utils.ValueStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class TmdbUserClient (private val context: Application, override val resources: Resources): BaseClient {
    private val persistClient: ValueStore by lazy {
        val storeKey = "cineast_prefs"
        PrefsStore(storeKey, context)
    }

    val authenticationApi: AuthenticationApi by lazy {
        retrofit.create(AuthenticationApi::class.java)
    }

    private val accountSerializer: Serializer<Account> by lazy {
        MoshiSerializer<Account>(Account::class.java)
    }


    fun getAccessToken(asyncResponse: AsyncResponse<AccessToken>) {
        authenticationApi.getAccessToken(RestUtils.API_KEY).enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>?) {
                Timber.d("AccessToken: $response")

                response?.let {
                    it.body()?.let {
                        it.request_token?.let {
                            persistClient.set(RestUtils.REQUEST_TOKEN_KEY, it)
                        }
                    }
                    asyncResponse.onSuccess(it.body())
                }
            }

            override fun onFailure(call: Call<AccessToken>, throwable: Throwable) {
                asyncResponse.onFail(ApiUtils.throwableToCineastError(throwable))
            }
        })
    }

    fun getSession(requestToken: String?, asyncResponse: AsyncResponse<String>) {
        authenticationApi.getSession(RestUtils.API_KEY, requestToken).enqueue(object : Callback<Session> {
            override fun onResponse(call: Call<Session>, response: Response<Session>) {
                response.body()?.session_id?.let {
                    persistClient.set(RestUtils.SESSION_ID_KEY, it)
                    asyncResponse.onSuccess(it)
                    setAccount(it)
                }
            }

            override fun onFailure(call: Call<Session>, t: Throwable) {
                Timber.d("session error: $t")
            }
        })
    }

    private fun setAccount(sessionId: String?) {
        sessionId?.let {
            authenticationApi.getAccount(RestUtils.API_KEY, it).enqueue(object : Callback<Account> {
                override fun onResponse(call: Call<Account>, response: Response<Account>) {
                    Timber.d("account: ${response.body()}")

                    response.body()?.let {
                        persistClient.set(RestUtils.ACCOUNT_ID_KEY, accountSerializer.toJson(it))
                    }

                }

                override fun onFailure(call: Call<Account>, t: Throwable) {
                    Timber.e("t: $t")
                }
            })
        }
    }


    fun getRequestToken(): String? {
        return persistClient.get(RestUtils.REQUEST_TOKEN_KEY, null)
    }

    fun logout() {
        persistClient.remove(RestUtils.SESSION_ID_KEY)
        persistClient.remove(RestUtils.REQUEST_TOKEN_KEY)
        persistClient.remove(RestUtils.ACCOUNT_ID_KEY)

    }

    fun isLoggedIn(): Boolean {
        return !persistClient.get(RestUtils.SESSION_ID_KEY, null).isNullOrEmpty()
    }

}