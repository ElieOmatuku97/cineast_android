package elieomatuku.cineast_android.vu

import android.app.Activity
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.DiscoverAdapter
import elieomatuku.cineast_android.business.business.model.data.*
import elieomatuku.cineast_android.presenter.DiscoverPresenter
import elieomatuku.cineast_android.utils.UiUtils
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.vu_discover.view.*

class DiscoverVu (inflater: LayoutInflater,
                  activity: Activity,
                  fragmentWrapper: FragmentWrapper?,
                  parentView: ViewGroup?) : BaseVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView){
    private val movieSelectPublisher: PublishSubject<Movie> by lazy {
        PublishSubject.create<Movie>()
    }
    val movieSelectObservable: Observable<Movie>
        get() = movieSelectPublisher.hide()


    private val personSelectPublisher: PublishSubject<Person> by lazy {
        PublishSubject.create<Person>()
    }

    val personSelectObservable: Observable <Person>
        get() = personSelectPublisher.hide()


    private val loginClickPublisher: PublishSubject<Boolean> by lazy {
        PublishSubject.create<Boolean>()
    }

    val loginClickObservable : Observable <Boolean>
        get() = loginClickPublisher.hide()

    private val listView : RecyclerView by lazy {
        rootView.recyclerview
    }

    val adapter: DiscoverAdapter by lazy {
         DiscoverAdapter(movieSelectPublisher, personSelectPublisher, loginClickPublisher)
    }

    override fun getRootViewLayoutId(): Int {
        return R.layout.vu_discover
    }

    override fun onCreate() {
        super.onCreate()
        listView.adapter = adapter
        val itemDecoration = DividerItemDecoration(listView.context, DividerItemDecoration.VERTICAL)
        val drawable: Drawable? = ResourcesCompat.getDrawable(activity.resources, R.drawable.item_decoration, activity.theme)
        if (drawable != null) {
            itemDecoration.setDrawable(drawable)
        }
        listView.addItemDecoration(itemDecoration)
        listView.layoutManager = LinearLayoutManager(activity)
    }

    fun setWigdet(popularPeople: List<People>, movieContainer: MovieContainer){
        adapter.widgetMap = getWidgetMap(popularPeople, movieContainer)
        adapter.notifyDataSetChanged()
        listView.visibility = View.VISIBLE
    }

    private fun getWidgetMap(popularPeople: List<People>, movieContainer: MovieContainer): MutableMap <String, List<Widget>> {
        return mutableMapOf<String, List<Widget>>(Pair(DiscoverPresenter.POPULAR_MOVIE_KEY, movieContainer.popularMovie), Pair(DiscoverPresenter.POPULAR_PEOPLE_KEY, popularPeople),
                 Pair(DiscoverPresenter.NOW_PLAYING_KEY, movieContainer.nowPlayingMovie), Pair(DiscoverPresenter.UPCOMING_MOVIE_KEY,movieContainer.upcomingMovie),
                 Pair(DiscoverPresenter.TOP_RATED_MOVIE_KEY,movieContainer.topRated))
    }


    fun gotoWebview(result: AccessToken?) {
        if (result != null) {
            val authenticateUrl = Uri.parse(activity.getString(R.string.authenticate_url))
                    .buildUpon()
                    .appendPath(result.request_token)
                    .build()
                    .toString()

            UiUtils.gotoWebview (authenticateUrl, activity as AppCompatActivity)
        }
    }
}