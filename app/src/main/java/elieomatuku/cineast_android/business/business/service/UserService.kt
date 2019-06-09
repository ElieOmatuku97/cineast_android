package elieomatuku.cineast_android.business.business.service

import android.app.Application
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.business.model.data.AccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class UserService (private val restService: RestService, private val application: Application) {


    fun getAccessToken (asyncResponse: AsyncResponse<AccessToken>) {
        restService.authenticationApi.getAccessToken(application.applicationContext.getString(R.string.api_key)).enqueue( object : Callback <AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                Timber.d("AccessToken: $response")
                asyncResponse.onSuccess(response.body())
            }

            override fun onFailure(call: Call<AccessToken>, throwable: Throwable) {
                asyncResponse.onFail(throwable.toString())
            }
        })
    }
}