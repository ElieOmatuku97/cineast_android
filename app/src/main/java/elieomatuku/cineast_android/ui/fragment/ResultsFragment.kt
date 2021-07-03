package elieomatuku.cineast_android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content

class ResultsFragment() : Fragment() {
    companion object {
        val LOG_TAG = ResultsFragment::class.java.simpleName

        fun newInstance(contents: List<Content>): ResultsFragment {
            val fragment = ResultsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_results, container, false)
    }
}
