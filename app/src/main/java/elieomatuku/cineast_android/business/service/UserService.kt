package elieomatuku.cineast_android.business.service

import android.app.Application
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.MoshiSerializer
import elieomatuku.cineast_android.business.client.Serializer
import elieomatuku.cineast_android.business.model.data.AccessToken
import elieomatuku.cineast_android.business.model.data.Account
import elieomatuku.cineast_android.business.model.data.Movie
import elieomatuku.cineast_android.business.model.data.Session
import elieomatuku.cineast_android.business.model.response.MovieResponse
import elieomatuku.cineast_android.business.rest.MovieApi
import elieomatuku.cineast_android.utils.RestUtils
import elieomatuku.cineast_android.utils.ValueStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class UserService (private val restService: RestService, private val movieApi: MovieApi, private val application: Application) {
    private val persistClient: ValueStore by lazy {
        val storeKey = "cineast_prefs"
        PrefsStore(storeKey, application)
    }

    private val accountSerializer: Serializer<Account> by lazy {
        MoshiSerializer<Account>(Account::class.java)
    }

    fun getAccessToken (asyncResponse: AsyncResponse<AccessToken>) {
        restService.authenticationApi.getAccessToken(application.applicationContext.getString(R.string.api_key)).enqueue( object : Callback <AccessToken> {
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
                asyncResponse.onFail(throwable.toString())
            }
        })
    }


    fun getSession(requestToken: String?,  asyncResponse: AsyncResponse<String>) {
        restService.authenticationApi.getSession(application.applicationContext.getString(R.string.api_key), requestToken).enqueue( object : Callback<Session> {
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

    fun setAccount(sessionId: String?)  {
        sessionId?.let {
            restService.authenticationApi.getAccount(application.applicationContext.getString(R.string.api_key), it).enqueue( object : Callback<Account> {
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


    fun getAccount(): Account? {
        return persistClient.get(RestUtils.ACCOUNT_ID_KEY, null)?.let {
             accountSerializer.fromJson(it)
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

    fun getWatchList(asyncResponse: AsyncResponse<List<Movie>>)  {
        persistClient.get(RestUtils.SESSION_ID_KEY, null)?.let {
            movieApi.getWatchList(application.applicationContext.getString(R.string.api_key), it).enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    response.body()?.results?.let {
                        asyncResponse.onSuccess(it)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    asyncResponse.onFail(t.toString())
                }
            })
        }
    }
}