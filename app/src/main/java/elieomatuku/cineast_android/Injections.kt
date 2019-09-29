package elieomatuku.cineast_android

import android.app.Application
import android.content.Context
import android.content.res.Resources
import elieomatuku.cineast_android.business.client.RestClient
import elieomatuku.cineast_android.business.rest.RestApi
import elieomatuku.cineast_android.business.service.ConnectionService
import elieomatuku.cineast_android.business.service.DiscoverService
import elieomatuku.cineast_android.business.service.UserService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


fun depInjecT(app: Application): Kodein {

    return Kodein.lazy {
        bind<Application>() with instance(app)
        bind<Context>() with instance(app.applicationContext)
        bind<Resources>() with instance(app.applicationContext.resources)
        bind<RestClient>() with singleton { RestClient(instance()) }
        bind<RestApi>() with singleton { RestApi(instance()) }
        bind<DiscoverService>() with singleton { DiscoverService(instance()) }
        bind<UserService>() with singleton {
            UserService(instance(), instance())
        }
        bind<ConnectionService>() with singleton { ConnectionService(instance()) }
    }

}