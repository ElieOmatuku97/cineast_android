package elieomatuku.cineast_android.search.movie

import elieomatuku.cineast_android.presenter.PresenterCacheLazy
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment


class MoviesSearchFragment : MVPFragment<MoviesSearchPresenter, MoviesSearchVu>() {
    companion object {
        private val MVP_UID by lazy {
            MoviesSearchFragment.hashCode()
        }

        fun newInstance(): MoviesSearchFragment {
            return MoviesSearchFragment()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<MoviesSearchPresenter, MoviesSearchVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ MoviesSearchPresenter() }),
                ::MoviesSearchVu)
    }
}