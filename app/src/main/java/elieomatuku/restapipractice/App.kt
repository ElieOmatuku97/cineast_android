package elieomatuku.restapipractice

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.squareup.leakcanary.LeakCanary
import elieomatuku.restapipractice.business.business.service.RestService
import elieomatuku.restapipractice.business.business.service.DiscoverService
import elieomatuku.restapipractice.business.business.client.RestClient
import elieomatuku.restapipractice.business.business.service.UserService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import timber.log.Timber
import timber.log.Timber.DebugTree




class App: Application() {

    companion object {
        lateinit var kodein: Kodein
            private set

    }

    override fun onCreate() {
        super.onCreate()

        val that = this
        kodein = Kodein {
            bind<Application>() with instance(that)
            bind<Context>() with instance(applicationContext)
            bind<Resources>() with instance(applicationContext.resources)
            bind<RestClient>() with singleton {RestClient(instance())}
            bind<RestService>() with singleton { RestService(instance()) }
            bind<DiscoverService>() with singleton { DiscoverService(instance()) }
            bind<UserService>() with singleton {
                UserService(instance(), instance())
            }
        }


        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

}