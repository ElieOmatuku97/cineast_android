package elieomatuku.cineast_android.ui.search.people

import android.content.Intent
import android.os.Bundle
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.ui.contents.ContentGridFragment
import elieomatuku.cineast_android.ui.details.people.PeopleActivity
import elieomatuku.cineast_android.utils.Constants


/**
 * Created by elieomatuku on 2021-06-05
 */

class PeopleGridFragment : ContentGridFragment<PeopleGridViewModel>(PeopleGridViewModel::class.java) {
    companion object {
        const val SCREEN_NAME = "Search"
        const val CONTENT_KEY = "peopleApi"

        fun newInstance(): ContentGridFragment<PeopleGridViewModel> {
            return PeopleGridFragment()
        }
    }

    override fun gotoContent(content: Content) {
        val params = Bundle()
        params.putString(Constants.SCREEN_NAME_KEY, SCREEN_NAME)
        params.putParcelable(CONTENT_KEY, content)
        gotoPeople(params)
    }

    private fun gotoPeople(params: Bundle) {
        val intent = Intent(activity, PeopleActivity::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}