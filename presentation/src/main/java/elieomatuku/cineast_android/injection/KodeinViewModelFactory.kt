package elieomatuku.cineast_android.injection

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.factory
import org.kodein.di.instanceOrNull


/**
 * Created by elieomatuku on 2021-09-11
 */

class KodeinViewModelFactory(private val injector: DI) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return injector.direct.instanceOrNull<ViewModel>(tag = modelClass.simpleName) as T?
            ?: modelClass.newInstance()
    }
}

class KodeinAbstractSavedStateViewModelFactory(private val injector: DI) :
    AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val viewModelFactory: ((SavedStateHandle) -> ViewModel) = injector.direct.factory()
        val modelClassFromFactory = viewModelFactory(handle)

        return if (modelClass.isInstance(modelClassFromFactory)) {
            modelClassFromFactory as T? ?: modelClass.newInstance()
        } else {
            injector.direct.instanceOrNull<ViewModel>(tag = modelClass.simpleName) as T?
                ?: modelClass.newInstance()
        }
    }
}