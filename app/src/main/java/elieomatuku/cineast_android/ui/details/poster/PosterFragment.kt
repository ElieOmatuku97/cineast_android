package elieomatuku.cineast_android.ui.details.poster

import android.os.Bundle
import elieomatuku.cineast_android.ui.common_presenter.PresenterCacheLazy
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment

class PosterFragment : MVPFragment<PosterPresenter, PosterVu>() {
    companion object {
        private val MVP_UID by lazy {
            PosterFragment.hashCode()
        }

        fun newInstance(args: Bundle): PosterFragment {
            val fragment = PosterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<PosterPresenter, PosterVu> {
        return MVPDispatcher(
            MVP_UID,
            // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
            PresenterCacheLazy({ PosterPresenter() }),
            ::PosterVu
        )
    }
}
