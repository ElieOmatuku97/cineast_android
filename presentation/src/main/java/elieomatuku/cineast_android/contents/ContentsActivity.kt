package elieomatuku.cineast_android.contents

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.base.BaseActivity
import elieomatuku.cineast_android.databinding.ActivityContentBinding
import elieomatuku.cineast_android.details.movie.MovieFragment
import elieomatuku.cineast_android.details.person.PersonFragment
import elieomatuku.cineast_android.utils.Constants
import elieomatuku.cineast_android.viewholder.MovieItem
import java.io.Serializable

class ContentsActivity : BaseActivity() {
    companion object {
        const val SCREEN_NAME = "Discover"
        const val WIDGET_KEY = "content"
        const val MOVIE_KEY = "movieApi"
        const val PEOPLE_KEY = "peopleApi"

        fun startActivity(context: Context, contents: List<Content>?, screenNameRes: Int? = null) {
            val intent = Intent(context, ContentsActivity::class.java)
            val params = Bundle()
            params.putSerializable(
                Constants.WIDGET_KEY,
                contents as Serializable
            )

            if (screenNameRes != null) {
                params.putInt(Constants.SCREEN_NAME_KEY, screenNameRes)
            }

            intent.putExtras(params)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val contents: List<Content> = intent.getSerializableExtra(WIDGET_KEY) as List<Content>
        val screenNameRes = intent.getIntExtra(Constants.SCREEN_NAME_KEY, 0)

        updateView(contents, screenNameRes)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!super.onSupportNavigateUp()) {
            onBackPressed()
        }
        return true
    }

    private fun updateView(contents: List<Content>, screenNameRes: Int? = null) {
        setToolbarTitle(screenNameRes)
        setContents(contents)
    }

    private fun setToolbarTitle(screenNameRes: Int? = null) {
        binding.toolbar.title = screenNameRes?.let {
            resources.getString(it)
        } ?: resources.getString(R.string.nav_title_discover)
    }

    private fun setContents(contents: List<Content>) {
        binding.composeView.setContent {
            ContentScreen(contents = contents) { content ->
                val params = Bundle()

                params.putString(Constants.SCREEN_NAME_KEY, SCREEN_NAME)

                if (content is Person) {
                    params.putSerializable(PEOPLE_KEY, content)
                    gotoContent(params, PersonFragment::class.java)
                } else {
                    params.putSerializable(MOVIE_KEY, content)
                    gotoContent(params, MovieFragment::class.java)
                }
            }
        }
    }

    private fun gotoContent(params: Bundle, contentActivityClass: Class<*>) {
        val intent = Intent(this, contentActivityClass)
        intent.putExtras(params)
        startActivity(intent)
    }
}

@Composable
fun ContentScreen(contents: List<Content>, onContentClick: (content: Content) -> Unit) {
    LazyColumn {
        items(contents) { content ->
            when (content) {
                is Person -> {
                    elieomatuku.cineast_android.viewholder.ContentItem(
                        imagePath = content.profilePath,
                        title = content.name
                    )
                }
                is Movie -> {
                    MovieItem(movie = content)
                }
            }
        }
    }
}
