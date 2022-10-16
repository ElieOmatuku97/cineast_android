package elieomatuku.cineast_android.injection

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.factory
import org.kodein.di.generic.instanceOrNull


/**
 * Created by elieomatuku on 2021-09-11
 */

class KodeinViewModelFactory(private val injector: Kodein) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return injector.direct.instanceOrNull<ViewModel>(tag = modelClass.simpleName) as T?
            ?: modelClass.newInstance()
    }
}

class KodeinAbstractSavedStateViewModelFactory(private val injector: Kodein) :
    AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val viewModelFactory: ((SavedStateHandle) -> ViewModel) = injector.direct.factory()
        return viewModelFactory(handle) as T? ?: modelClass.newInstance()
    }
}