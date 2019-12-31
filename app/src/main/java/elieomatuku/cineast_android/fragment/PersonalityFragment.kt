package elieomatuku.cineast_android.fragment

import elieomatuku.cineast_android.presenter.PersonalityPresenter
import elieomatuku.cineast_android.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.vu.PersonalityVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment

class PersonalityFragment : MVPFragment <PersonalityPresenter, PersonalityVu> () {
    companion object {
        private val MVP_UID by lazy {
            hashCode()
        }

        fun newInstance(): PersonalityFragment {
            return PersonalityFragment()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<PersonalityPresenter, PersonalityVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ PersonalityPresenter() }),
                ::PersonalityVu)
    }
}