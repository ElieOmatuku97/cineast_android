package elieomatuku.cineast_android

import android.content.Context
import elieomatuku.cineast_android.database.ContentDatabase
import elieomatuku.cineast_android.database.repository.ContentRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


/**
 * Created by elieomatuku on 2019-12-16
 */


object DatabaseKodeinModule {
    const val moduleName = "Database"

    fun getModule(context: Context): Kodein.Module {

        return Kodein.Module(name = moduleName) {
            // Database

            bind<ContentDatabase>() with singleton {
                ContentDatabase.getInstance(context) }

            bind<ContentRepository>() with singleton {
                ContentRepository(instance())
            }
        }
    }

}