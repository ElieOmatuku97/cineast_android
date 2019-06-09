package elieomatuku.restapipractice.adapter


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import elieomatuku.restapipractice.fragment.MoviePosterFragment
import elieomatuku.restapipractice.business.business.model.data.Poster
import elieomatuku.restapipractice.presenter.MovieGalleryPresenter


class MovieGalleryPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
    var posters: List<Poster>? = null

    override fun getItem(position: Int): Fragment {
        val args = Bundle()

        posters?.get(position)?.file_path.let {
            args.putString(MovieGalleryPresenter.MOVIE_POSTER_PATH, it)
        }

        Log.d(MovieGalleryPresenter::class.simpleName, "Args: $args")
        return MoviePosterFragment.newInstance(args)
    }

    override fun getCount(): Int {
        return posters?.size ?: 0
    }
}