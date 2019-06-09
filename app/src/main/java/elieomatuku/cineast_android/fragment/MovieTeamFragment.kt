package elieomatuku.cineast_android.fragment

import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.business.business.model.data.Cast
import elieomatuku.cineast_android.business.business.model.data.Crew
import elieomatuku.cineast_android.presenter.MovieTeamPresenter
import elieomatuku.cineast_android.presenter.PresenterCacheLazy
import elieomatuku.cineast_android.vu.MovieTeamVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.MVPFragment
import java.util.ArrayList


class MovieTeamFragment:  MVPFragment <MovieTeamPresenter, MovieTeamVu>()  {
    companion object {
        const val MOVIE_CAST = "movie_cast"
        const val MOVIE_CREW = "movie_crew"
        const val MOVIE_TITLE = "movie_title"

        private val MVP_UID by lazy {
            MovieTeamFragment.hashCode()
        }

        fun newInstance(cast: List<Cast>?, crew: List<Crew>?, movieTitle: String?): MovieTeamFragment {
            val args = Bundle()
            args.putParcelableArrayList(MOVIE_CAST, cast as ArrayList<out Parcelable>)
            args.putParcelableArrayList(MOVIE_CREW, crew as ArrayList<out Parcelable>)
            args.putString(MOVIE_TITLE, movieTitle)

            val fragment = MovieTeamFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<MovieTeamPresenter, MovieTeamVu> {
        return MVPDispatcher(MVP_UID,
                // Using PresenterCacheLazy since PresenterCacheLoaderCallback gives issues where presenter is null in onSaveState
                PresenterCacheLazy({ MovieTeamPresenter() }),
                ::MovieTeamVu)
    }
}