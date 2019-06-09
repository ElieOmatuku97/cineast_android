package elieomatuku.restapipractice.fragment


import elieomatuku.restapipractice.presenter.DiscoverPresenter
import elieomatuku.restapipractice.presenter.PresenterCacheLazy
import elieomatuku.restapipractice.vu.DiscoverVu
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