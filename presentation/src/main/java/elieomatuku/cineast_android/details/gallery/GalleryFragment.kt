package elieomatuku.cineast_android.details.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.eftimoff.viewpagertransformers.TabletTransformer
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.databinding.FragmentGalleryBinding

class GalleryFragment : BaseFragment() {
    companion object {
        const val POSTERS = "posters"
        const val MOVIE_POSTER_PATH = "movie_poster_path"

        fun newInstance(): GalleryFragment {
            return GalleryFragment()
        }
    }

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val args: GalleryFragmentArgs by navArgs()

    private val galleryPagerAdapter: GalleryPagerAdapter by lazy {
        GalleryPagerAdapter(childFragmentManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewpagerImages.setPageTransformer(true, TabletTransformer())
        binding.viewpagerImages.adapter = galleryPagerAdapter
        galleryPagerAdapter.notifyDataSetChanged()

        binding.viewpagerImages.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }
        })

        updateView(args.posters.toList())

        binding.galleryWidgetCloseIcon.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateView(postersPaths: List<String>) {
        galleryPagerAdapter.postersPaths = postersPaths
        galleryPagerAdapter.notifyDataSetChanged()
    }
}
