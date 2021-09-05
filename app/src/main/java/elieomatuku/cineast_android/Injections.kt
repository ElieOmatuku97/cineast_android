package elieomatuku.cineast_android

import android.app.Application
import android.content.Context
import android.content.res.Resources
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.business.client.TmdbUserClient
import elieomatuku.cineast_android.business.service.ConnectionService
import elieomatuku.cineast_android.business.service.ContentService
import elieomatuku.cineast_android.cache.DatabaseKodeinModule
import elieomatuku.cineast_android.data.AuthenticationRepositoryImpl
import elieomatuku.cineast_android.data.MovieRepositoryImpl
import elieomatuku.cineast_android.data.PersonRepositoryImpl
import elieomatuku.cineast_android.domain.repository.AuthenticationRepository
import elieomatuku.cineast_android.domain.repository.MovieRepository
import elieomatuku.cineast_android.domain.repository.PersonRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun depInjecT(app: Application): Kodein {

    return Kodein.lazy {
        bind<Application>() with instance(app)
        bind<Context>() with instance(app.applicationContext)
        bind<Resources>() with instance(app.applicationContext.resources)
        bind<ContentService>() with singleton { ContentService(instance(), instance()) }
        bind<ConnectionService>() with singleton { ConnectionService(instance()) }

        bind<TmdbUserClient>() with singleton {
            TmdbUserClient(instance(), instance())
        }

        bind<TmdbContentClient>() with singleton { TmdbContentClient(instance(), instance()) }

        importOnce(DatabaseKodeinModule.getModule(app))

        bind<MovieRepository>() with singleton {
            MovieRepositoryImpl(instance())
        }

        bind<AuthenticationRepository>() with singleton {
            AuthenticationRepositoryImpl(instance())
        }

        bind<PersonRepository>() with singleton {
            PersonRepositoryImpl(instance())
        }
    }
}
