package elieomatuku.cineast_android.business.business.client

import android.content.res.Resources
import elieomatuku.cineast_android.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RestClient (resources: Resources) {

    val restAdapter: Retrofit by lazy {
        Retrofit.Builder()
                   .baseUrl(resources.getString(R.string.rest_base_url))
                   .addConverterFactory(GsonConverterFactory.create())
                   .build()
    }

}