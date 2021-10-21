package elieomatuku.cineast_android.base

import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import elieomatuku.cineast_android.extensions.lifecycleAwareLazy
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import elieomatuku.cineast_android.extensions.*


/**
 * Created by elieomatuku on 2021-10-10
 */

abstract class BaseDialogFragment : DialogFragment(), KodeinAware {

    override val kodein: Kodein by kodein()
    val viewModelFactory: ViewModelProvider.Factory by instance()

    protected inline fun <reified VM : ViewModel> getViewModel(): VM =
        getViewModel(viewModelFactory)

    protected inline fun <reified VM : ViewModel> getSharedViewModel(): VM =
        getSharedViewModel(viewModelFactory)

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> = lifecycleAwareLazy(this) {
        getViewModel<VM>()
    }
}