package elieomatuku.cineast_android.search.people

import android.content.Intent
import android.os.Bundle
import android.view.View
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.contents.ContentGridFragment
import elieomatuku.cineast_android.details.person.PersonActivity
import elieomatuku.cineast_android.utils.*

/**
 * Created by elieomatuku on 2021-06-05
 */

class PeopleGridFragment :
    ContentGridFragment() {
    companion object {
        const val SCREEN_NAME = "Search"
        const val CONTENT_KEY = "peopleApi"

        fun newInstance(): ContentGridFragment {
            return PeopleGridFragment()
        }
    }

    private val viewModel: PeopleGridViewModel by viewModel<PeopleGridViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            state.viewError.consume {
                updateErrorView(it.message)
            }

            state.contents?.let {
                updateView(it)
            }

        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun gotoContent(content: Content) {
        val params = Bundle()
        params.putString(Constants.SCREEN_NAME_KEY, SCREEN_NAME)
        params.putSerializable(CONTENT_KEY, content)
        gotoPeople(params)
    }

    private fun gotoPeople(params: Bundle) {
        val intent = Intent(activity, PersonActivity::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}
