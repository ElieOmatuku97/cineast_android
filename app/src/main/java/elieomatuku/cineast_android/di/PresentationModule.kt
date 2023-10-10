package elieomatuku.cineast_android.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import elieomatuku.cineast_android.connection.ConnectionService

@Module
@InstallIn(SingletonComponent::class)
abstract class PresentationModule {

    @Binds
    abstract fun bindConnectionService(
        connectionService: ConnectionService
    ): ConnectionService

}