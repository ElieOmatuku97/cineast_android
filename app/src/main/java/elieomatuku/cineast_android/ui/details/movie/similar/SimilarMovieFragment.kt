package elieomatuku.cineast_android.ui.details.movie.similar

import android.os.Bundle
import elieomatuku.cineast_android.core.model.MovieSummary
import elieomatuku.cineast_android.ui.common_presenter.PresenterCacheLazy
import elieomatuku.cineast_android.ui.details.MoviesVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment


class SimilarMovieFragment : MVPFragment<SimilarMoviePresenter, MoviesVu>() {
    companion object {
        const val MOVIE_SUMMARY = "movie_summary"

        private val MVP_UID by lazy {
            hashCode()
        }

        fun newInstance(movieSummary: MovieSummary): SimilarMovieFragment {
            val args = Bundle()
            args.putParcelable(MOVIE_SUMMARY, movieSummary)

            val fragment = SimilarMovieFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<SimilarMoviePresenter, MoviesVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy { SimilarMoviePresenter() },
                ::MoviesVu)
    }
}