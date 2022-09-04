package elieomatuku.cineast_android.search.movie

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
import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.contents.ContentGridFragment
import elieomatuku.cineast_android.details.movie.MovieFragment
import elieomatuku.cineast_android.utils.Constants
import elieomatuku.cineast_android.viewholder.EmptyStateItem
import java.io.Serializable

/**
 * Created by elieomatuku on 2021-06-05
 */

class MoviesGridFragment :
    ContentGridFragment() {
    companion object {
        const val CONTENT_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"
        const val SCREEN_NAME = "Search"
    }

    private var genres: List<Genre>? = listOf()
    private val viewModel: MoviesGridViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            updateView(state)

            state.genres?.let {
                genres = it
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun gotoContent(content: Content) {
        val params = Bundle()
        params.putString(Constants.SCREEN_NAME_KEY, SCREEN_NAME)
        params.putSerializable(CONTENT_KEY, content)
        params.putSerializable(MOVIE_GENRES_KEY, genres as Serializable)
        gotoMovie(params)
    }

    private fun gotoMovie(params: Bundle) {
        val intent = Intent(activity, MovieFragment::class.java)
        intent.putExtras(params)
        activity?.startActivity(intent)
    }
}

@Composable
fun MoviesGrid(
    viewModelFactory: ViewModelProvider.Factory,
    viewModel: MoviesGridViewModel = viewModel(factory = viewModelFactory),
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
