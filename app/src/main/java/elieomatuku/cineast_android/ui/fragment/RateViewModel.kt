package elieomatuku.cineast_android.ui.fragment

import androidx.lifecycle.viewModelScope
import elieomatuku.cineast_android.domain.interactor.movie.RateMovie
import elieomatuku.cineast_android.domain.interactor.runUseCase
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.ui.base.BaseViewModel
import kotlinx.coroutines.launch


/**
 * Created by elieomatuku on 2021-10-10
 */

class RateViewModel(private val rateMovie: RateMovie) : BaseViewModel<Unit>(Unit) {

    fun rateMovie(movie: Movie, rate: Double) {
        viewModelScope.launch {
            runUseCase(rateMovie, RateMovie.Input(movie, rate))
        }
    }
}