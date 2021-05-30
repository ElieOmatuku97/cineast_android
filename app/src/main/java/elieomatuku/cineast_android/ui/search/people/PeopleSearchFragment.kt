package elieomatuku.cineast_android.ui.search.people

import elieomatuku.cineast_android.ui.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.ui.search.ContentGridVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment

class PeopleSearchFragment : MVPFragment <PeopleSearchPresenter, ContentGridVu> () {
    companion object {
        private val MVP_UID by lazy {
            hashCode()
        }

        fun newInstance(): PeopleSearchFragment {
            return PeopleSearchFragment()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<PeopleSearchPresenter, ContentGridVu> {
        return MVPDispatcher(
            MVP_UID,
            // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
            PresenterCacheLazy { PeopleSearchPresenter() },
            ::ContentGridVu
        )
    }
}
