package elieomatuku.cineast_android.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged

/**
 * Created by elieomatuku on 2021-07-03
 */


abstract class BaseViewModel<T : Any>(initState: T) : ViewModel() {

    private val _viewState = MediatorLiveData<T>().apply { value = initState }
    val viewState = _viewState.distinctUntilChanged()
    protected var state
        get() = _viewState.value!!
        set(value) {
            _viewState.value = value
        }

    protected var stateAsync: T = state
        set(value) {
            _viewState.postValue(value)
        }

    protected fun <T> addStateSource(source: LiveData<T>, onChanged: (T) -> Unit) {
        _viewState.addSource(source, onChanged)
    }
}
