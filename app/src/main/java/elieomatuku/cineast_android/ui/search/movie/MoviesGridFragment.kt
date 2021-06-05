package elieomatuku.cineast_android.ui.search.movie

import elieomatuku.cineast_android.ui.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.ui.search.ContentGridVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment

class MoviesGridFragment : MVPFragment<MoviesGridPresenter, ContentGridVu>() {
    companion object {
        private val MVP_UID by lazy {
            MoviesGridFragment.hashCode()
        }

        fun newInstance(): MoviesGridFragment {
            return MoviesGridFragment()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<MoviesGridPresenter, ContentGridVu> {
        return MVPDispatcher(
            MVP_UID,
            // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
            PresenterCacheLazy { MoviesGridPresenter() },
            ::ContentGridVu
        )
    }
}
