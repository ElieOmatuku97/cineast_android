package elieomatuku.cineast_android.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import elieomatuku.cineast_android.cache.ContentDatabase
import elieomatuku.cineast_android.cache.PrefManagerImpl
import elieomatuku.cineast_android.data.PrefManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Provides
    @Singleton
    fun provideContentDatabase(@ApplicationContext context: Context): ContentDatabase {
        return ContentDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideGenreDao(contentDatabase: ContentDatabase) = contentDatabase.genreDao()

    @Provides
    @Singleton
    fun provideMovieDao(contentDatabase: ContentDatabase) = contentDatabase.movieDao()

    @Provides
    @Singleton
    fun provideMovieTypeDao(contentDatabase: ContentDatabase) = contentDatabase.movieTypeDao()

    @Provides
    @Singleton
    fun provideMovieTypeJoinDao(contentDatabase: ContentDatabase) = contentDatabase.movieTypeJoinDao()

    @Provides
    @Singleton
    fun providePersonDao(contentDatabase: ContentDatabase) = contentDatabase.personDao()

    @Provides
    @Singleton
    fun provideBreedCache(@ApplicationContext context: Context): PrefManager {
        val storeKey = "cineast_prefs"
        return PrefManagerImpl(storeKey, context)
    }

}