package elieomatuku.cineast_android.ui.contents

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.core.model.Movie
import elieomatuku.cineast_android.core.model.Person
import elieomatuku.cineast_android.ui.activity.BaseActivity
import elieomatuku.cineast_android.ui.details.movie.MovieActivity
import elieomatuku.cineast_android.ui.details.people.PeopleActivity
import elieomatuku.cineast_android.ui.discover.DiscoverPresenter
import elieomatuku.cineast_android.ui.vu.ListVu
import elieomatuku.cineast_android.utils.Constants
import elieomatuku.cineast_android.utils.UiUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_content.*
import java.util.ArrayList

class ContentsActivity : BaseActivity() {
    companion object {
        const val FIRST_WIDGET_TYPE_OCCURENCE = 0
        const val WIDGET_KEY = "content"
        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"
        const val PEOPLE_KEY = "peopleApi"

        fun startActivity(context: Context, contents: List<Content>, screenNameRes: Int? = null) {
            val intent = Intent(context, ContentsActivity::class.java)
            val params = Bundle()
            params.putParcelableArrayList(Constants.WIDGET_KEY, contents as ArrayList<out Parcelable>)

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
        ContentsAdapter(contentSelectPublisher, R.layout.holder_movie_list)
    }

    private val peopleAdapter: ContentsAdapter by lazy {
        ContentsAdapter(contentSelectPublisher, R.layout.holder_people_list)
    }

    private val listView: RecyclerView by lazy {
        list_view_container
    }

    private val viewModel: ContentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_content)

        val contents: List<Content>? = intent.getParcelableArrayListExtra(WIDGET_KEY)
        val screenNameRes = intent.getIntExtra(Constants.SCREEN_NAME_KEY, 0)

        updateView(contents, screenNameRes)
    }

    override fun onResume() {
        super.onResume()

        rxSubs.add(
                contentSelectObservable
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe { content: Content ->
                            val params = Bundle()

                            params.putString(Constants.SCREEN_NAME_KEY, DiscoverPresenter.SCREEN_NAME)

                            if (content is Person) {
                                params.putParcelable(PEOPLE_KEY, content)
                                gotoContent(params, PeopleActivity::class.java)
                            } else {
                                params.putParcelable(MOVIE_KEY, content)
                                params.putParcelableArrayList(MOVIE_GENRES_KEY, viewModel.genresLiveData.value as ArrayList<out Parcelable>)
                                gotoContent(params, MovieActivity::class.java)
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

    private fun updateView(contents: List<Content>?, screenNameRes: Int? = null) {
        setToolbarTitle(screenNameRes)
        contents?.let {
            if (it.isNotEmpty()) {
                setUpListView(it)
            }
        }
    }

    private fun setToolbarTitle(screenNameRes: Int? = null) {
        toolbar?.title = screenNameRes?.let {
            resources.getString(it)
        } ?: resources.getString(R.string.nav_title_discover)
    }

    private fun setUpListView(contents: List<Content>) {
        contentsAdapter = if (areWidgetsMovies(contents)) moviesAdapter else peopleAdapter
        contentsAdapter.contents = contents.toMutableList()
        listView.adapter = contentsAdapter
        contentsAdapter.notifyDataSetChanged()
    }

    private fun areWidgetsMovies(contents: List<Content?>): Boolean {
        return (contents[FIRST_WIDGET_TYPE_OCCURENCE] != null) && (contents[ListVu.FIRST_WIDGET_TYPE_OCCURENCE] is Movie)
    }

    private fun gotoContent(params: Bundle, contentActivityClass: Class<*>) {
        val intent = Intent(this, contentActivityClass)
        intent.putExtras(params)
        startActivity(intent)
    }
}
