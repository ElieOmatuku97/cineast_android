package elieomatuku.cineast_android.ui.details.people.knownfor

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.core.model.CineastError
import elieomatuku.cineast_android.core.model.Genre
import elieomatuku.cineast_android.core.model.KnownFor
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.ui.common_presenter.BasePresenter
import elieomatuku.cineast_android.ui.details.MoviesFragment.Companion.MOVIE_GENRES_KEY
import elieomatuku.cineast_android.ui.details.MoviesVu
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.ArrayList

class KnownForPresenter : BasePresenter<MoviesVu>() {
    companion object {
        const val PEOPLE_CAST_KEY = "people_cast"
        const val MOVIE_KEY = "movieApi"
        const val PEOPLE_NAME_KEY = "people_name"
        const val SCREEN_NAME_KEY = "screen_name"
    }

    private var genres: List<Genre>? = listOf()

    override fun onLink(vu: MoviesVu, inState: Bundle?, args: Bundle) {
        super.onLink(vu, inState, args)

        val knownFor: List<KnownFor>? = args.getParcelableArrayList(PEOPLE_CAST_KEY)
        val peopleName: String? = args.getString(PEOPLE_NAME_KEY)
        knownFor?.let {
            vu.updateVu(knownFor.mapNotNull { it.toMovie() }, R.string.cast)
        }

        rxSubs.add(
            contentService.genres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Timber.d("genres from database: $it")
                        genres = it
                    },
                    { error ->
                        Timber.e("Unable to get genres $error")
                    }
                )
        )

        rxSubs.add(
            vu.movieSelectObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { movie: Movie ->
                        getMovie(movie.id, peopleName)
                    },
                    { t: Throwable ->
                        Timber.e("movieSelectObservable failed:$t")
                    }
                )
        )
    }

    private fun getMovie(movieId: Int, peopleName: String?) {
        contentService.getMovie(
            movieId,
            object : AsyncResponse<Movie> {
                override fun onSuccess(response: Movie?) {
                    val movie: Movie = response as Movie
                    handler.post {
                        val params = Bundle()
                        peopleName?.let {
                            params.putString(SCREEN_NAME_KEY, it)
                        }

                        params.putParcelable(MOVIE_KEY, movie)
                        params.putParcelableArrayList(MOVIE_GENRES_KEY, genres as ArrayList<out Parcelable>)
                        vu?.gotoMovie(params)
                    }
                }

                override fun onFail(error: CineastError) {
                    Timber.e("error: $error")
                }
            }
        )
    }
}
