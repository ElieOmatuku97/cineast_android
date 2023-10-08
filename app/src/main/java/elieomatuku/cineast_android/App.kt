package elieomatuku.cineast_android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.kodein.di.DI
import org.kodein.di.DIAware
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp
class App : Application(), DIAware {

    override val di: DI
        get() = depInjecT(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
