package elieomatuku.cineast_android.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import elieomatuku.cineast_android.R
import kotlinx.android.synthetic.main.fragment_bareoverview.*

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return LayoutInflater.from(context)
            .inflate(R.layout.fragment_bareoverview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val overview: String? = arguments?.getString(OVERVIEW, null)
        val overviewTitle: String? = arguments?.getString(OVERVIEW_TITLE, null)
        summary_title_view.text = overviewTitle
        summary_view.text = overview
    }
}
