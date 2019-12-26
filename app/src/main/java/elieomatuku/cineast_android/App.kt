package elieomatuku.cineast_android

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import org.kodein.di.Kodein
import timber.log.Timber
import timber.log.Timber.DebugTree




class App: Application() {

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

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this)
        // Normal app init code...
    }

}