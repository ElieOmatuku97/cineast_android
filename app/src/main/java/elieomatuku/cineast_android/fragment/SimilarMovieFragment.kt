package elieomatuku.cineast_android.fragment

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.business.business.model.data.Movie
import elieomatuku.cineast_android.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.presenter.SimilarMoviePresenter
import elieomatuku.cineast_android.vu.SimilarMovieVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment
import java.util.ArrayList

class SimilarMovieFragment: MVPFragment<SimilarMoviePresenter, SimilarMovieVu>() {
    companion object {
        const val MOVIE_SIMILAR_MOVIES = "movie_similar_movies"
        const val MOVIE_TITLE = "movie_title"

        private val MVP_UID by lazy {
            SimilarMovieFragment.hashCode()
        }

        fun newInstance(similarMovies: List<Movie>?, movieTitle: String?): SimilarMovieFragment{
            val args = Bundle()
            args.putParcelableArrayList(MOVIE_SIMILAR_MOVIES, similarMovies as ArrayList<out Parcelable>)
            args.putString(MOVIE_TITLE, movieTitle)
            val fragment = SimilarMovieFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<SimilarMoviePresenter, SimilarMovieVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ SimilarMoviePresenter() }),
                ::SimilarMovieVu)
    }
}