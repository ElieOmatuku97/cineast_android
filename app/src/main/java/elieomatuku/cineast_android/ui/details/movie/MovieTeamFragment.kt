package elieomatuku.cineast_android.ui.details.movie

import android.os.Bundle
import elieomatuku.cineast_android.core.model.MovieSummary
import elieomatuku.cineast_android.ui.presenter.PresenterCacheLazy
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment


class MovieTeamFragment : MVPFragment<MovieTeamPresenter, MovieTeamVu>() {
    companion object {
        const val MOVIE_SUMMARY = "movie_summary"

        private val MVP_UID by lazy {
            hashCode()
        }

        fun newInstance(movieSummary: MovieSummary): MovieTeamFragment {
            val args = Bundle()

            args.putParcelable(MOVIE_SUMMARY, movieSummary)

            val fragment = MovieTeamFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<MovieTeamPresenter, MovieTeamVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ MovieTeamPresenter() }),
                ::MovieTeamVu)
    }
}