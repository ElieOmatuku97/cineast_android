package elieomatuku.cineast_android.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import elieomatuku.cineast_android.data.AuthenticationRepositoryImpl
import elieomatuku.cineast_android.data.MovieRepositoryImpl
import elieomatuku.cineast_android.data.PersonRepositoryImpl
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository
import elieomatuku.cineast_android.domain.repository.MovieRepository
import elieomatuku.cineast_android.domain.repository.PersonRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    abstract fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    abstract fun bindPersonRepository(
        personRepositoryImpl: PersonRepositoryImpl
    ): PersonRepository
}