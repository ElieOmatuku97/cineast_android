package elieomatuku.cineast_android.fragment

import elieomatuku.cineast_android.presenter.PopularMoviesPresenter
import elieomatuku.cineast_android.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.vu.PopularMoviesVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment


class PopularMoviesFragment : MVPFragment <PopularMoviesPresenter, PopularMoviesVu>() {
    companion object {
        private val MVP_UID by lazy {
            PopularMoviesFragment.hashCode()
        }

        fun newInstance(): PopularMoviesFragment {
            return PopularMoviesFragment()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<PopularMoviesPresenter, PopularMoviesVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ PopularMoviesPresenter() }),
                ::PopularMoviesVu)
    }
}