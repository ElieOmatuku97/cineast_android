package elieomatuku.cineast_android.vu

import android.app.Activity
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import com.eftimoff.viewpagertransformers.TabletTransformer
import elieomatuku.cineast_android.adapter.MovieGalleryPagerAdapter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.business.model.data.Poster
import io.chthonic.mythos.mvp.FragmentWrapper
import kotlinx.android.synthetic.main.fragment_image_gallery.view.*


class MovieGalleryVu(inflater: LayoutInflater,
                     activity: Activity,
                     fragmentWrapper: FragmentWrapper?,
                     parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    private val viewPager: ViewPager by lazy {
        rootView.viewpager_images
    }

    private val closeIconView: ImageButton by lazy {
        rootView.gallery_widget_close_icon
    }

    private val movieGalleryPagerAdapter: MovieGalleryPagerAdapter by lazy {
        val fm =  fragmentWrapper?.support?.childFragmentManager

//        creates leaks, not too sure, looks supportFragmentManager holds reference to MovieGalleryFragment
//        val fm = (activity as AppCompatActivity).supportFragmentManager
        MovieGalleryPagerAdapter(fm!!)
    }

    override fun getRootViewLayoutId(): Int {
        return R.layout.fragment_image_gallery
    }

    override fun onCreate() {
        viewPager.setPageTransformer(true, TabletTransformer())
        viewPager.adapter = movieGalleryPagerAdapter
        movieGalleryPagerAdapter.notifyDataSetChanged()

        viewPager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {

            }

        })

        closeIconView.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager?.popBackStack()
        }
    }

    fun updateImages(posters: List<Poster> ) {
        movieGalleryPagerAdapter.posters = posters
        movieGalleryPagerAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.adapter = null
    }
}