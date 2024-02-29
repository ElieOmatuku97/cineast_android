package elieomatuku.cineast_android.presentation.fragment

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import elieomatuku.cineast_android.domain.interactor.movie.RateMovie
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by elieomatuku on 2021-10-10
 */

@HiltViewModel
class RateViewModel @Inject constructor(private val rateMovie: RateMovie) :
    BaseViewModel<Unit>(Unit) {

    fun rateMovie(movie: Movie, rate: Double) {
        viewModelScope.launch {
            runUseCase(rateMovie, RateMovie.Input(movie, rate))
        }
    }
}