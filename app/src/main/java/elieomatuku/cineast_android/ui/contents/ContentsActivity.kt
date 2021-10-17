package elieomatuku.cineast_android.ui.contents

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.ui.extensions.Contents
import elieomatuku.cineast_android.ui.base.BaseActivity
import elieomatuku.cineast_android.ui.utils.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_content.*
import java.util.ArrayList

class ContentsActivity : BaseActivity() {
    companion object {
        const val SCREEN_NAME = "Discover"
        const val WIDGET_KEY = "content"
        const val MOVIE_KEY = "movieApi"
        const val MOVIE_GENRES_KEY = "genres"
        const val PEOPLE_KEY = "peopleApi"

        fun startActivity(context: Context, contents: List<Content>?, screenNameRes: Int? = null) {
            val intent = Intent(context, ContentsActivity::class.java)
            val params = Bundle()
            params.putParcelableArrayList(
                Constants.WIDGET_KEY,
                contents as ArrayList<out Parcelable>
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
        ContentsAdapter(contentSelectPublisher, R.layout.holder_movie_list)
    }

    private val peopleAdapter: ContentsAdapter by lazy {
        ContentsAdapter(contentSelectPublisher, R.layout.holder_people_list)
    }

    private val listView: RecyclerView by lazy {
        list_view_container
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_content)

        val contents: Contents? = intent.getParcelableExtra(WIDGET_KEY)
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

                    params.putString(Constants.SCREEN_NAME_KEY, SCREEN_NAME)

//                    if (content is Person) {
//                        params.putParcelable(PEOPLE_KEY, content)
//                        gotoContent(params, PeopleActivity::class.java)
//                    } else {
//                        params.putParcelable(MOVIE_KEY, content)
//                        params.putParcelableArrayList(
//                            MOVIE_GENRES_KEY,
//                            viewModel.genresLiveData.value as ArrayList<out Parcelable>
//                        )
//                        gotoContent(params, MovieActivity::class.java)
//                    }
                }
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!super.onSupportNavigateUp()) {
            onBackPressed()
        }
        return true
    }

    private fun updateView(contents: Contents?, screenNameRes: Int? = null) {
        setToolbarTitle(screenNameRes)
        contents?.let {
            setContents(contents)
        }
    }

    private fun setToolbarTitle(screenNameRes: Int? = null) {
        toolbar?.title = screenNameRes?.let {
            resources.getString(it)
        } ?: resources.getString(R.string.nav_title_discover)
    }

    private fun setContents(contents: Contents) {
        contentsAdapter = if (areWidgetsMovies(contents)) moviesAdapter else peopleAdapter
        contentsAdapter.contents = contents.value?.toMutableList() ?: mutableListOf()
        listView.adapter = contentsAdapter
        contentsAdapter.notifyDataSetChanged()
    }

    private fun areWidgetsMovies(contents: Contents): Boolean {
        val firstElement: Content? = contents.value?.first()
        return (firstElement != null) && (firstElement is Movie)
    }

    private fun gotoContent(params: Bundle, contentActivityClass: Class<*>) {
        val intent = Intent(this, contentActivityClass)
        intent.putExtras(params)
        startActivity(intent)
    }
}
