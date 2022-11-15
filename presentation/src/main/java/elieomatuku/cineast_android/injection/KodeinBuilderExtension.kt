package elieomatuku.cineast_android.injection

import androidx.lifecycle.ViewModel
import org.kodein.di.DI
import org.kodein.di.bind


/**
 * Created by elieomatuku on 2021-09-11
 */

inline fun <reified T : ViewModel> DI.Builder.bindViewModel(overrides: Boolean? = null): DI.Builder.TypeBinder<T> {
    return bind<T>(T::class.java.simpleName, overrides)
}