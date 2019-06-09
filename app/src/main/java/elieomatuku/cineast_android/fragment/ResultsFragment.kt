package elieomatuku.restapipractice.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.restapipractice.R
import elieomatuku.restapipractice.business.business.model.data.Widget

class ResultsFragment(): Fragment() {
    companion object {
        val LOG_TAG = ResultsFragment::class.java.simpleName

        fun newInstance(widgets: List<Widget>): ResultsFragment{
            val fragment =  ResultsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.vu_results, container, false)
        return view
    }

}