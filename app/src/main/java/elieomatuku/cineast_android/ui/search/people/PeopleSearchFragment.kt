package elieomatuku.cineast_android.ui.search.people

import elieomatuku.cineast_android.ui.common_presenter.PresenterCacheLazy
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment

class PeopleSearchFragment : MVPFragment <PeopleSearchPresenter, PeopleSearchVu> () {
    companion object {
        private val MVP_UID by lazy {
            hashCode()
        }

        fun newInstance(): PeopleSearchFragment {
            return PeopleSearchFragment()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<PeopleSearchPresenter, PeopleSearchVu> {
        return MVPDispatcher(
            MVP_UID,
            // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
            PresenterCacheLazy({ PeopleSearchPresenter() }),
            ::PeopleSearchVu
        )
    }
}
