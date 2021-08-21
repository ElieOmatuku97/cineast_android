package elieomatuku.cineast_android.business.client

import android.content.res.Resources
import elieomatuku.cineast_android.business.api.AuthenticationApi
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.domain.ValueStore
import elieomatuku.cineast_android.domain.model.AccessToken
import elieomatuku.cineast_android.domain.model.Account
import elieomatuku.cineast_android.domain.model.Session
import elieomatuku.cineast_android.utils.ApiUtils
import elieomatuku.cineast_android.utils.RestUtils
import okhttp3.Interceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class TmdbUserClient(
    override val resources: Resources,
    override val persistClient: ValueStore,
    override val interceptor: Interceptor? = null
) : BaseClient {

    private val authenticationApi: AuthenticationApi by lazy {
        retrofit.create(AuthenticationApi::class.java)
    }

    private val accountSerializer: Serializer<Account> by lazy {
        MoshiSerializer<Account>(Account::class.java)
    }

    fun getAccessToken(asyncResponse: AsyncResponse<AccessToken>) {
        authenticationApi.getAccessToken().enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>?) {
                Timber.d("AccessToken: $response")

                response?.let {
                    it.body()?.let {
                        it.requestToken?.let {
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

    fun getSession(requestToken: String?, asyncResponse: AsyncResponse<Pair<String, Account>>) {
        authenticationApi.getSession(requestToken).enqueue(object : Callback<Session> {
            override fun onResponse(call: Call<Session>, response: Response<Session>) {
                response.body()?.sessionId?.let {
                    persistClient.set(RestUtils.SESSION_ID_KEY, it)
                    setAccount(it, asyncResponse)
                }
            }

            override fun onFailure(call: Call<Session>, t: Throwable) {
                Timber.d("session error: $t")
            }
        })
    }

    private fun setAccount(sessionId: String?, asyncResponse: AsyncResponse<Pair<String, Account>>) {
        sessionId?.let { sessionId ->
            authenticationApi.getAccount(sessionId).enqueue(object : Callback<Account> {
                override fun onResponse(call: Call<Account>, response: Response<Account>) {
                    Timber.d("account: ${response.body()}")

                    response.body()?.let {
                        persistClient.set(RestUtils.ACCOUNT_ID_KEY, accountSerializer.toJson(it))
                        it.username?.let { username ->
                            persistClient.set(RestUtils.ACCOUNT_USERNAME, username)
                        }
                        asyncResponse.onSuccess(Pair(sessionId, it))
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

    fun getUsername(): String? {
        return persistClient.get(RestUtils.ACCOUNT_USERNAME, null)
    }

    fun logout() {
        persistClient.remove(RestUtils.SESSION_ID_KEY)
        persistClient.remove(RestUtils.REQUEST_TOKEN_KEY)
        persistClient.remove(RestUtils.ACCOUNT_ID_KEY)
        persistClient.remove(RestUtils.ACCOUNT_USERNAME)
    }

    fun isLoggedIn(): Boolean {
        return !persistClient.get(RestUtils.SESSION_ID_KEY, null).isNullOrEmpty()
    }
}
