package elieomatuku.cineast_android.contents

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.connection.ConnectionService
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.extensions.asListOfType
import elieomatuku.cineast_android.injection.KodeinAbstractSavedStateViewModelFactory
import elieomatuku.cineast_android.utils.Constants
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.io.Serializable

class ContentsActivity : ComponentActivity(), KodeinAware {
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

    override val kodein: Kodein by closestKodein()
    private val connectionService: ConnectionService by instance()
    val viewModelFactory: AbstractSavedStateViewModelFactory by lazy {
        KodeinAbstractSavedStateViewModelFactory(kodein)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contents: List<Content> = intent.getSerializableExtra(WIDGET_KEY) as List<Content>
        val screenNameRes = intent.getIntExtra(Constants.SCREEN_NAME_KEY, 0)

        setContent {
            ContentsNavGraph(
                contents = contents,
                viewModelFactory = viewModelFactory,
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

    private fun setToolbarTitle(screenNameRes: Int? = null) {
//        binding.toolbar.title = screenNameRes?.let {
//            resources.getString(it)
//        } ?: resources.getString(R.string.nav_title_discover)
    }
}