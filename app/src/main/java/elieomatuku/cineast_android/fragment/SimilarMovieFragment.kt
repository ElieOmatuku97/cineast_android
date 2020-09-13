package elieomatuku.cineast_android.fragment

import android.os.Bundle
import elieomatuku.cineast_android.core.model.MovieSummary
import elieomatuku.cineast_android.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.presenter.SimilarMoviePresenter
import elieomatuku.cineast_android.vu.SimilarMovieVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment


class SimilarMovieFragment : MVPFragment<SimilarMoviePresenter, SimilarMovieVu>() {
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

    override fun createMVPDispatcher(): MVPDispatcher<SimilarMoviePresenter, SimilarMovieVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy { SimilarMoviePresenter() },
                ::SimilarMovieVu)
    }
}