package elieomatuku.cineast_android.ui.details.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.eftimoff.viewpagertransformers.TabletTransformer
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Poster
import elieomatuku.cineast_android.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : BaseFragment() {
    companion object {
        const val POSTERS = "posters"
        const val MOVIE_POSTER_PATH = "movie_poster_path"

        fun newInstance(): GalleryFragment {
            return GalleryFragment()
        }
    }

    private val viewPager: ViewPager by lazy {
        viewpager_images
    }

    private val closeIconView: ImageButton by lazy {
        gallery_widget_close_icon
    }

    private val galleryPagerAdapter: GalleryPagerAdapter by lazy {
        GalleryPagerAdapter(checkNotNull(childFragmentManager))
    }

    private var posters: List<Poster> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.getParcelableArrayList<Poster>(POSTERS)?.let {
            posters = it
        }

        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager.setPageTransformer(true, TabletTransformer())
        viewPager.adapter = galleryPagerAdapter
        galleryPagerAdapter.notifyDataSetChanged()

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
            }
        })

        updateView(posters)

        closeIconView.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.popBackStack()
        }
    }

    private fun updateView(posters: List<Poster>) {
        galleryPagerAdapter.posters = posters
        galleryPagerAdapter.notifyDataSetChanged()
    }
}
