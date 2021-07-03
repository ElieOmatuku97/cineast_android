package elieomatuku.cineast_android.domain

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

/**
 * Created by elieomatuku on 2019-12-23
 */

object CoreKodeinModule {

    const val moduleName = "Core"

    fun getModule(): Kodein.Module {
        return Kodein.Module(name = moduleName) {
            bind<ValueStore>() with singleton {
                val storeKey = "cineast_prefs"
                PrefsStore(storeKey, instance())
            }
        }
    }
}
