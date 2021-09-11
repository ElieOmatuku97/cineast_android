package elieomatuku.cineast_android

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application(), KodeinAware {
    companion object {
        lateinit var getKodein: Kodein
            private set
    }

    override val kodein: Kodein
        get() = depInjecT(this)

    override fun onCreate() {
        super.onCreate()

        getKodein = depInjecT(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
