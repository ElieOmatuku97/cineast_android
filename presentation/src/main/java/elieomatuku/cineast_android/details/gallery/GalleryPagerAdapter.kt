package elieomatuku.cineast_android.details.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class GalleryPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {
    var postersPaths: List<String> = listOf()

    override fun getItem(position: Int): Fragment {
        val args = Bundle()

        if (postersPaths.isNotEmpty()) {
            args.putString(GalleryFragment.MOVIE_POSTER_PATH, postersPaths[position])
        }

        return GalleryPosterFragment.newInstance(args)
    }

    override fun getCount(): Int {
        return postersPaths.size
    }
}