package elieomatuku.cineast_android.ui.settings.user_movies

import android.os.Handler
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elieomatuku.cineast_android.App
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.business.service.ContentService
import elieomatuku.cineast_android.domain.interactor.movie.GetFavorites
import elieomatuku.cineast_android.domain.interactor.movie.GetGenres
import elieomatuku.cineast_android.domain.interactor.movie.GetTopRatedMovies
import elieomatuku.cineast_android.domain.interactor.movie.GetWatchList
import elieomatuku.cineast_android.domain.model.CineastError
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.ui.base.BaseViewModel
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import timber.log.Timber

/**
 * Created by elieomatuku on 2021-05-29
 */

class UserMoviesViewModel(
    private val getGenres: GetGenres,
    private val getFavorites: GetFavorites,
    private val getWatchList: GetWatchList
) : BaseViewModel<Unit>(Unit) {

    private val tmdbContentClient: TmdbContentClient by App.getKodein.instance()
    private val contentService: ContentService by App.getKodein.instance()

    val genresLiveData: LiveData<List<Genre>> = LiveDataReactiveStreams.fromPublisher(contentService.genres().subscribeOn(Schedulers.io()).toFlowable())
    val userMovies = MutableLiveData<List<Movie>>()
    val errorMessage = MutableLiveData<String>()

    private val handler: Handler = Handler()

    fun getFavourites() {
        GlobalScope.launch {
            val movieResponse = tmdbContentClient.getFavoriteList()
            if (movieResponse.isSuccess) {
                val movies = movieResponse.getOrNull()?.results
                movies?.let {
                    launch(Dispatchers.Main) {
                        userMovies.value = it
                    }
                }
            } else {
                errorMessage.value = movieResponse.exceptionOrNull()?.message
            }
        }
    }

    fun getWatchList() {
        GlobalScope.launch {
            val movieResponse = tmdbContentClient.getWatchList()
            if (movieResponse.isSuccess) {
                val movies = movieResponse.getOrNull()?.results
                movies?.let {
                    launch(Dispatchers.Main) {
                        userMovies.value = it
                    }
                }
            } else {
                launch(Dispatchers.Main) {
                    Timber.e("error : ${movieResponse.exceptionOrNull()}")
                    errorMessage.value = movieResponse.exceptionOrNull()?.message
                }
            }
        }
    }

    fun getRatedMovies() {
        tmdbContentClient.getUserRatedMovies(object : AsyncResponse<List<Movie>> {
            override fun onSuccess(result: List<Movie>?) {
                handler.post {
                    result?.let {
                        userMovies.value = it
                    }
                }
            }

            override fun onFail(error: CineastError) {
                Timber.e("error : $error")
                error.let {
                    errorMessage.value = it.statusMessage!!
                }
            }
        })
    }

    fun removeMovieFromWatchList(movie: Movie) {
        tmdbContentClient.removeMovieFromWatchList(movie)
    }

    fun removeMovieFromFavorites(movie: Movie) {
        tmdbContentClient.removeMovieFromFavoriteList(movie)
    }
}
