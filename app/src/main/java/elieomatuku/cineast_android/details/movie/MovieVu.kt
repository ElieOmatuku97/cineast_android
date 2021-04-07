package elieomatuku.cineast_android.details.movie

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.*
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.MovieSummaryAdapter
import kotlinx.android.synthetic.main.vu_movie.view.*
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.test.espresso.idling.CountingIdlingResource
import elieomatuku.cineast_android.details.gallery.GalleryFragment
import elieomatuku.cineast_android.core.model.*
import elieomatuku.cineast_android.details.gallery.GalleryPresenter
import elieomatuku.cineast_android.utils.DividerItemDecorator
import elieomatuku.cineast_android.vu.ToolbarVu
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.ArrayList


class MovieVu(inflater: LayoutInflater,
              activity: Activity,
              fragmentWrapper: FragmentWrapper?,
              parentView: ViewGroup?) : ToolbarVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

    companion object {
        const val MOVIE_OVERVIEW = "overview"
        const val MOVIE_CREW = "crew"
        const val SIMILAR_MOVIES = "similar_movies"
    }

    override val toolbar: Toolbar?
        get() = rootView.toolbar

    override fun getRootViewLayoutId(): Int {
        return R.layout.vu_movie
    }

    private val listView: RecyclerView by lazy {
        rootView.list_view_container
    }

    private val onProfileClickedPicturePublisher: PublishSubject<Int> by lazy {
        PublishSubject.create<Int>()
    }

    val onProfileClickedPictureObservable: Observable<Int>
        get() = onProfileClickedPicturePublisher.hide()


    private val segmentedButtonsPublisher: PublishSubject<Pair<String, MovieSummary>> by lazy {
        PublishSubject.create<Pair<String, MovieSummary>>()
    }
    val segmentedButtonsObservable: Observable<Pair<String, MovieSummary>>
        get() = segmentedButtonsPublisher.hide()

    val moviePresentedPublisher: PublishSubject<Movie>? by lazy {
        if (activity is MovieActivity) {
            activity.moviePresentedPublisher
        } else {
            null
        }
    }

    val watchListCheckPublisher: PublishSubject<Boolean>? by lazy {
        if (activity is MovieActivity) {
            activity.watchListCheckPublisher
        } else {
            null
        }
    }

    val favoriteListCheckPublisher: PublishSubject<Boolean>? by lazy {
        if (activity is MovieActivity) {
            activity.favoriteListCheckPublisher
        } else {
            null
        }
    }

    private val adapter: MovieSummaryAdapter by lazy {
        MovieSummaryAdapter(onProfileClickedPicturePublisher, segmentedButtonsPublisher)
    }

    /**
     * Only used for UI test, instance of [CountingIdlingResource].
     */
    val countingIdlingResource = CountingIdlingResource("Movie loader")

    override fun onCreate() {
        super.onCreate()
        setUpListView()
    }

    private fun setUpListView() {
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity)

        val itemDecorationDrawable = ResourcesCompat.getDrawable(activity.resources, R.drawable.item_decoration, activity.theme)
        val dividerItemDecoration = DividerItemDecorator(itemDecorationDrawable)

        listView.addItemDecoration(dividerItemDecoration)
    }

    fun showMovie(movieSummary: MovieSummary) {
        Timber.d("showMovie: movie summary = $movieSummary")

        toolbar?.title = movieSummary.screenName
        adapter.movieSummary = movieSummary
        adapter.notifyDataSetChanged()

        val overViewFragment = OverviewFragment.newInstance(movieSummary)
        updateContainer(overViewFragment)

        /**
         * This decrement idle resource method, used for espresso ui test.
         */
        countingIdlingResource.decrement()
    }

    fun goToGallery(posters: List<Poster>?) {
        val galleryFragment = GalleryFragment.newInstance()
        val args = Bundle()
        args.putParcelableArrayList(GalleryPresenter.POSTERS, posters as ArrayList<out Parcelable>)
        galleryFragment.arguments = args

        if (activity is AppCompatActivity) {
            activity.supportFragmentManager.beginTransaction().add(android.R.id.content, galleryFragment, null).addToBackStack(null).commit()
        }
    }

    fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
        listView.visibility = View.VISIBLE
    }

    fun gotoTab(displayAndMovieSummary: Pair<String, MovieSummary>) {
        val fragment = when (displayAndMovieSummary.first) {
            SIMILAR_MOVIES -> {
                SimilarMovieFragment.newInstance(displayAndMovieSummary.second)
            }
            MOVIE_CREW -> {
                MovieTeamFragment.newInstance(displayAndMovieSummary.second)
            }
            MOVIE_OVERVIEW -> {
                OverviewFragment.newInstance(displayAndMovieSummary.second)
            }
            else -> null
        }

        if (fragment != null) {
            updateContainer(fragment)
        }
    }

    private fun updateContainer(fragment: Fragment) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        }
    }
}