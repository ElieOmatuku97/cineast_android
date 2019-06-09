package elieomatuku.cineast_android.fragment

import android.os.Bundle
import elieomatuku.cineast_android.presenter.MoviePosterPresenter
import elieomatuku.cineast_android.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.vu.MoviePosterVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment

class MoviePosterFragment: MVPFragment<MoviePosterPresenter, MoviePosterVu>() {
    companion object {
        private val MVP_UID by lazy {
            MoviePosterFragment.hashCode()
        }

        fun newInstance(args: Bundle): MoviePosterFragment{
            val fragment = MoviePosterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<MoviePosterPresenter, MoviePosterVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ MoviePosterPresenter() }),
                ::MoviePosterVu)
    }
}