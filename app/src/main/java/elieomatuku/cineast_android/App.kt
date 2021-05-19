package elieomatuku.cineast_android

import android.app.Application
import org.kodein.di.Kodein
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {

    companion object {
        lateinit var kodein: Kodein
            private set
    }

    override fun onCreate() {
        super.onCreate()

        kodein = depInjecT(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
