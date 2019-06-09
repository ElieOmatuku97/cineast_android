package elieomatuku.cineast_android.fragment


import elieomatuku.cineast_android.presenter.DiscoverPresenter
import elieomatuku.cineast_android.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.vu.DiscoverVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment




class DiscoverFragment: MVPFragment<DiscoverPresenter, DiscoverVu>(){
    companion object {
        private val MVP_UID by lazy {
            DiscoverFragment.hashCode()
        }

        fun newInstance(): DiscoverFragment{
            return DiscoverFragment()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<DiscoverPresenter, DiscoverVu> {
        return MVPDispatcher(MVP_UID,
//                 Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ DiscoverPresenter() }),
                ::DiscoverVu)
    }
}