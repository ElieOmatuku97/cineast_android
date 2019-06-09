package elieomatuku.cineast_android.fragment

import elieomatuku.cineast_android.presenter.PopularPeoplePresenter
import elieomatuku.cineast_android.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.vu.PopularPeopleVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment

class PopularPeopleFragment : MVPFragment <PopularPeoplePresenter, PopularPeopleVu> () {
    companion object {
        private val MVP_UID by lazy {
            PopularPeopleFragment.hashCode()
        }

        fun newInstance(): PopularPeopleFragment {
            return PopularPeopleFragment()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<PopularPeoplePresenter, PopularPeopleVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ PopularPeoplePresenter() }),
                ::PopularPeopleVu)
    }
}