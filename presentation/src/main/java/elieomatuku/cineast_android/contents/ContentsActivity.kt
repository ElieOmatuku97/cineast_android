package elieomatuku.cineast_android.contents

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.base.BaseActivity
import elieomatuku.cineast_android.details.movie.MovieFragment
import elieomatuku.cineast_android.details.person.PersonFragment
import elieomatuku.cineast_android.utils.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_content.*
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

    private val contentSelectPublisher: PublishSubject<Content> by lazy {
        PublishSubject.create<Content>()
    }

    private val contentSelectObservable: Observable<Content>
        get() = contentSelectPublisher.hide()

    lateinit var contentsAdapter: ContentsAdapter

    private val moviesAdapter: ContentsAdapter by lazy {
        ContentsAdapter(contentSelectPublisher)
    }

    private val peopleAdapter: ContentsAdapter by lazy {
        ContentsAdapter(contentSelectPublisher)
    }

    private val listView: RecyclerView by lazy {
        list_view_container
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_content)

        val contents: List<Content> = intent.getSerializableExtra(WIDGET_KEY) as List<Content>
        val screenNameRes = intent.getIntExtra(Constants.SCREEN_NAME_KEY, 0)

        updateView(contents, screenNameRes)
    }

    override fun onResume() {
        super.onResume()

        rxSubs.add(
            contentSelectObservable
                .subscribe { content: Content ->
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
        )
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
        toolbar?.title = screenNameRes?.let {
            resources.getString(it)
        } ?: resources.getString(R.string.nav_title_discover)
    }

    private fun setContents(contents: List<Content>) {
        contentsAdapter = if (areWidgetsMovies(contents)) moviesAdapter else peopleAdapter
        contentsAdapter.contents = contents.toMutableList()
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = contentsAdapter
        contentsAdapter.notifyDataSetChanged()
    }

    private fun areWidgetsMovies(contents: List<Content>): Boolean {
        val firstElement: Content = contents.first()
        return firstElement is Movie
    }

    private fun gotoContent(params: Bundle, contentActivityClass: Class<*>) {
        val intent = Intent(this, contentActivityClass)
        intent.putExtras(params)
        startActivity(intent)
    }
}
