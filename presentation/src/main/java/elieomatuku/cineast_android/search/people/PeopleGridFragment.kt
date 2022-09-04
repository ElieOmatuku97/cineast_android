package elieomatuku.cineast_android.search.people

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import elieomatuku.cineast_android.contents.ContentGrid
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.contents.ContentGridFragment
import elieomatuku.cineast_android.details.person.PersonFragment
import elieomatuku.cineast_android.utils.*
import elieomatuku.cineast_android.viewholder.EmptyStateItem

/**
 * Created by elieomatuku on 2021-06-05
 */

class PeopleGridFragment :
    ContentGridFragment() {
    companion object {
        const val SCREEN_NAME = "Search"
        const val CONTENT_KEY = "peopleApi"
    }

    private val viewModel: PeopleGridViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            updateView(state)
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
        val intent = Intent(activity, PersonFragment::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}

@Composable
fun PeopleGrid(
    viewModelFactory: ViewModelProvider.Factory,
    viewModel: PeopleGridViewModel = viewModel(factory = viewModelFactory),
    hasNetworkConnection: Boolean,
    onContentClick: (content: Content) -> Unit
) {
    val viewState by viewModel.viewState.observeAsState()

    viewState?.contents?.let { contents ->
        ContentGrid(contents = contents) {
            onContentClick(it)
        }
    }

    viewState?.viewError?.apply {
        EmptyStateItem(
            errorMsg = peek().message,
            hasNetworkConnection = hasNetworkConnection
        )
    }
}
