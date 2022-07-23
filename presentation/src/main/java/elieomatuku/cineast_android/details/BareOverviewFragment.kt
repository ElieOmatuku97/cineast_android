package elieomatuku.cineast_android.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.google.accompanist.appcompattheme.AppCompatTheme
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.databinding.FragmentBareoverviewBinding

class BareOverviewFragment : Fragment() {
    companion object {
        private const val OVERVIEW = "overview"
        private const val OVERVIEW_TITLE = "overview_title"

        fun newInstance(overviewTitle: String?, overview: String?): BareOverviewFragment {
            val args = Bundle()
            args.putString(OVERVIEW, overview)
            args.putString(OVERVIEW_TITLE, overviewTitle)

            val fragment = BareOverviewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentBareoverviewBinding? = null
    private val binding: FragmentBareoverviewBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentBareoverviewBinding.bind(
                inflater.inflate(
                    R.layout.fragment_bareoverview,
                    container,
                    false
                )
            )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bareOverviewWidget.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        binding.bareOverviewWidget.setContent {
            AppCompatTheme {
                BareOverviewWidget(
                    title = arguments?.getString(OVERVIEW_TITLE, null) ?: String(),
                    overview = arguments?.getString(OVERVIEW, null) ?: String()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

@Composable
fun BareOverviewWidget(
    title: String = stringResource(id = R.string.plot_summary),
    overview: String = String()
) {
    Column {
        Text(
            text = title,
            color = colorResource(id = R.color.color_white),
            fontSize = dimensionResource(id = R.dimen.toolbar_text_size).value.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.holder_movie_layout_padding),
                    start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                )
        )
        Text(
            text = overview,
            color = colorResource(id = R.color.color_white),
            fontSize = dimensionResource(id = R.dimen.holder_movie_facts_text_size).value.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                    start = dimensionResource(id = R.dimen.holder_item_movie_textview_margin),
                    end = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right),
                    bottom = dimensionResource(id = R.dimen.holder_movie_facts_textview_padding_right)
                )
        )
    }
}
