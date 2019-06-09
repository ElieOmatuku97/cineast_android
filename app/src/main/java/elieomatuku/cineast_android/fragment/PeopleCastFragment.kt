package elieomatuku.cineast_android.fragment

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.business.business.model.data.PeopleCast
import elieomatuku.cineast_android.presenter.PeopleCastPresenter
import elieomatuku.cineast_android.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.vu.PeopleCastVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment


class PeopleCastFragment : MVPFragment<PeopleCastPresenter, PeopleCastVu>() {
    companion object {
        private val MVP_UID by lazy {
            PeopleCastFragment.hashCode()
        }
        const val PEOPLE_CAST = "people_cast"
        const val PEOPLE_NAME_KEY = "people_name"
        fun newInstance(peopleMovies: List<PeopleCast>, peopleName: String?): PeopleCastFragment {
            val args = Bundle()
            args.putParcelableArrayList(PEOPLE_CAST, peopleMovies as ArrayList<out Parcelable>)

            if (peopleName != null)
                args.putString(PEOPLE_NAME_KEY, peopleName)
            val fragment = PeopleCastFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<PeopleCastPresenter, PeopleCastVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ PeopleCastPresenter() }),
                ::PeopleCastVu)
    }
}