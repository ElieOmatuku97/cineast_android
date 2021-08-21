package elieomatuku.cineast_android.ui.details.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import elieomatuku.cineast_android.domain.model.Image
import timber.log.Timber

class GalleryPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {
    var posters: List<Image> = listOf()

    override fun getItem(position: Int): Fragment {
        val args = Bundle()

        if (posters.isNotEmpty()) {
            posters[position].filePath.let {
                args.putString(GalleryFragment.MOVIE_POSTER_PATH, it)
            }
        }

        Timber.d("getItem: Args =  $args")
        return GalleryPosterFragment.newInstance(args)
    }

    override fun getCount(): Int {
        return posters.size
    }
}
