package elieomatuku.cineast_android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import elieomatuku.cineast_android.BuildConfig
import elieomatuku.cineast_android.data.repository.authentication.AuthenticationRemote
import elieomatuku.cineast_android.data.repository.movie.MovieRemote
import elieomatuku.cineast_android.data.repository.person.PersonRemote
import elieomatuku.cineast_android.remote.AuthenticationRemoteImpl
import elieomatuku.cineast_android.remote.MovieRemoteImpl
import elieomatuku.cineast_android.remote.PersonRemoteImpl
import elieomatuku.cineast_android.remote.api.AuthenticationApi
import elieomatuku.cineast_android.remote.api.MovieApi
import elieomatuku.cineast_android.remote.api.PersonApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        val logLevel = HttpLoggingInterceptor.Level.BODY
        logging.level = logLevel

        val builder = OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.api_key)
                    .build()

                val request = original.newBuilder()
                    .method(original.method, original.body)
                    .url(url)
                    .build()

                chain.proceed(request)
            }
            .addInterceptor(logging)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .writeTimeout(30000, TimeUnit.MILLISECONDS)

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePersonApi(retrofit: Retrofit) = retrofit.create(PersonApi::class.java)

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit) = retrofit.create(MovieApi::class.java)

    @Provides
    @Singleton
    fun provideAuthenticationApi(retrofit: Retrofit) =
        retrofit.create(AuthenticationApi::class.java)

    @Provides
    @Singleton
    fun provideMovieRemote(movieRemote: MovieRemoteImpl): MovieRemote = movieRemote

    @Provides
    @Singleton
    fun provideAuthenticationRemote(authenticationRemote: AuthenticationRemoteImpl): AuthenticationRemote =
        authenticationRemote

    @Provides
    @Singleton
    fun providePersonRemote(personRemote: PersonRemoteImpl): PersonRemote =
        personRemote


}