package elieomatuku.cineast_android.ui.details.gallery

import elieomatuku.cineast_android.ui.presenter.PresenterCacheLazy
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment

class GalleryFragment : MVPFragment<GalleryPresenter, GalleryVu>() {
    companion object {
        private val MVP_UID by lazy {
            hashCode()
        }

        fun newInstance(): GalleryFragment {
            return GalleryFragment()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<GalleryPresenter, GalleryVu> {
        return MVPDispatcher(
            MVP_UID,
//                 Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
            PresenterCacheLazy({ GalleryPresenter() }),
            ::GalleryVu
        )
    }
}
