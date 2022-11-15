package elieomatuku.cineast_android.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import elieomatuku.cineast_android.extensions.lifecycleAwareLazy
import elieomatuku.cineast_android.extensions.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance

/**
 * Created by elieomatuku on 2021-10-17
 */

abstract class BasePreferenceFragmentCompat : PreferenceFragmentCompat(), DIAware {

    override val di: DI by closestDI()
    val viewModelFactory: ViewModelProvider.Factory by instance()

    protected inline fun <reified VM : ViewModel> getViewModel(): VM =
        getViewModel(viewModelFactory)

    protected inline fun <reified VM : ViewModel> getSharedViewModel(): VM =
        getSharedViewModel(viewModelFactory)

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> = lifecycleAwareLazy(this) {
        getViewModel<VM>()
    }

    protected inline fun <reified VM : ViewModel> sharedViewModel(): Lazy<VM> =
        lifecycleAwareLazy(this) {
            getSharedViewModel<VM>()
        }
}