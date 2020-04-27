package elieomatuku.cineast_android.vu

import android.app.Activity
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import com.eftimoff.viewpagertransformers.TabletTransformer
import elieomatuku.cineast_android.adapter.GalleryPagerAdapter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Poster
import io.chthonic.mythos.mvp.FragmentWrapper
import kotlinx.android.synthetic.main.fragment_gallery.view.*


class GalleryVu(inflater: LayoutInflater,
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

    private val galleryPagerAdapter: GalleryPagerAdapter by lazy {
        val fm =  fragmentWrapper?.support?.childFragmentManager
        GalleryPagerAdapter(fm!!)
    }

    override fun getRootViewLayoutId(): Int {
        return R.layout.fragment_gallery
    }

    override fun onCreate() {
        viewPager.setPageTransformer(true, TabletTransformer())
        viewPager.adapter = galleryPagerAdapter
        galleryPagerAdapter.notifyDataSetChanged()

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {

            }

        })

        closeIconView.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.popBackStack()
        }
    }

    fun updateImages(posters: List<Poster> ) {
        galleryPagerAdapter.posters = posters
        galleryPagerAdapter.notifyDataSetChanged()
    }
}