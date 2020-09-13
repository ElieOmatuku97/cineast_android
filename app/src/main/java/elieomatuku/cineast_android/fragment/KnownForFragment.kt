package elieomatuku.cineast_android.fragment

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.core.model.KnownFor
import elieomatuku.cineast_android.presenter.KnownForPresenter
import elieomatuku.cineast_android.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.vu.KnownForVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment


class KnownForFragment : MVPFragment<KnownForPresenter, KnownForVu>() {
    companion object {
        private val MVP_UID by lazy {
            hashCode()
        }
        private const val PEOPLE_CAST = "people_cast"
        private const val PEOPLE_NAME_KEY = "people_name"
        fun newInstance(peopleMovies: List<KnownFor>, peopleName: String?): KnownForFragment {
            val args = Bundle()
            args.putParcelableArrayList(PEOPLE_CAST, peopleMovies as ArrayList<out Parcelable>)

            if (peopleName != null)
                args.putString(PEOPLE_NAME_KEY, peopleName)
            val fragment = KnownForFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<KnownForPresenter, KnownForVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy { KnownForPresenter() },
                ::KnownForVu)
    }
}