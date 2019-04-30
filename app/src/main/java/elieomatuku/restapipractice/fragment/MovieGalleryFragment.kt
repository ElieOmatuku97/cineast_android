package elieomatuku.restapipractice.fragment


import elieomatuku.restapipractice.presenter.MovieGalleryPresenter
import elieomatuku.restapipractice.presenter.PresenterCacheLazy
import elieomatuku.restapipractice.vu.MovieGalleryVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment


class MovieGalleryFragment: MVPFragment<MovieGalleryPresenter, MovieGalleryVu>() {
    companion object {
        val TAG: String by lazy {
            MovieGalleryFragment::class.java.simpleName
        }

        private val MVP_UID by lazy {
            MovieGalleryFragment.hashCode()
        }

        fun newInstance(): MovieGalleryFragment{
            return MovieGalleryFragment()
        }
    }


    override fun createMVPDispatcher(): MVPDispatcher<MovieGalleryPresenter, MovieGalleryVu> {
        return MVPDispatcher(MVP_UID,
//                 Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ MovieGalleryPresenter() }),
                ::MovieGalleryVu)
    }
}