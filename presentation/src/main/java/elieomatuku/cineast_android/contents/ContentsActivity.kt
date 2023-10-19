package elieomatuku.cineast_android.contents

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.AndroidEntryPoint
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.extensions.asListOfType
import elieomatuku.cineast_android.materialtheme.ui.theme.AppTheme
import elieomatuku.cineast_android.utils.Constants
import elieomatuku.cineast_android.utils.connection.ConnectionService
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class ContentsActivity : ComponentActivity() {
    companion object {
        const val WIDGET_KEY = "content"

        fun startActivity(context: Context, contents: List<Content>?, screenNameRes: Int? = null) {
            val intent = Intent(context, ContentsActivity::class.java)
            val params = Bundle()
            params.putSerializable(
                Constants.WIDGET_KEY, contents as Serializable
            )

            if (screenNameRes != null) {
                params.putInt(Constants.SCREEN_NAME_KEY, screenNameRes)
            }

            intent.putExtras(params)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var connectionService: ConnectionService

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contents: List<Content> = intent.getSerializableExtra(WIDGET_KEY) as List<Content>
        val screenNameRes = intent.getIntExtra(Constants.SCREEN_NAME_KEY, 0)

        setContent {
            AppTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(stringResource(id = screenNameRes))
                            }
                        )

                    }) {
                    val paddingValues = it
                    ContentsNavGraph(
                        modifier = Modifier.padding(paddingValues),
                        contents = contents,
                        hasNetworkConnection = connectionService.hasNetworkConnection
                    ) {
                        it.asListOfType<Movie>()?.let { movies ->
                            startActivity(
                                this,
                                movies,
                                R.string.movies
                            )
                        }

                        it.asListOfType<Person>()?.let { people ->
                            startActivity(
                                this,
                                people,
                                R.string.people
                            )
                        }
                    }
                }
            }
        }
    }
}