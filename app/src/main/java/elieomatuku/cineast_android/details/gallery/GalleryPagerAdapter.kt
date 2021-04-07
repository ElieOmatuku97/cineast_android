package elieomatuku.cineast_android.details.gallery


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import elieomatuku.cineast_android.details.poster.PosterFragment
import elieomatuku.cineast_android.core.model.Poster
import timber.log.Timber


class GalleryPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    var posters: List<Poster> = listOf()

    override fun getItem(position: Int): Fragment {
        val args = Bundle()

        if (posters.isNotEmpty()) {
            posters[position].file_path.let {
                args.putString(GalleryPresenter.MOVIE_POSTER_PATH, it)
            }
        }

        Timber.d("getItem: Args =  $args")
        return PosterFragment.newInstance(args)
    }

    override fun getCount(): Int {
        return posters.size
    }
}