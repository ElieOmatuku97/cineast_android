package elieomatuku.cineast_android.business.client

import android.content.res.Resources
import elieomatuku.cineast_android.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RestClient (resources: Resources) {

    val restAdapter: Retrofit by lazy {
        Retrofit.Builder()
                   .baseUrl(resources.getString(R.string.rest_base_url))
                   .addConverterFactory(GsonConverterFactory.create())
                   .client(httpClient)
                   .build()
    }


    private val httpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
//        val logLevel = when (config.logLevel) {
//            CosmosCoreConfig.LogLevel.Debug -> HttpLoggingInterceptor.Level.BODY
//            CosmosCoreConfig.LogLevel.Info -> HttpLoggingInterceptor.Level.BASIC
//            else -> HttpLoggingInterceptor.Level.NONE
//        }

        val logLevel = HttpLoggingInterceptor.Level.BODY
        logging.level = logLevel

        OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .writeTimeout(30000, TimeUnit.MILLISECONDS)
                .build()
    }


}