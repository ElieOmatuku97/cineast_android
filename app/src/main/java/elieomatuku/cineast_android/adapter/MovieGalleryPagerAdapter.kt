package elieomatuku.cineast_android.adapter


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.util.Log
import elieomatuku.cineast_android.fragment.MoviePosterFragment
import elieomatuku.cineast_android.business.model.data.Poster
import elieomatuku.cineast_android.presenter.MovieGalleryPresenter


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