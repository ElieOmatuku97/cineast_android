package elieomatuku.cineast_android.vu

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.*
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.MovieAdapter
import kotlinx.android.synthetic.main.vu_movie.view.*
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import elieomatuku.cineast_android.fragment.MovieGalleryFragment
import elieomatuku.cineast_android.fragment.OverviewFragment
import elieomatuku.cineast_android.activity.MovieActivity
import elieomatuku.cineast_android.business.model.data.*
import elieomatuku.cineast_android.presenter.MovieGalleryPresenter
import elieomatuku.cineast_android.utils.DividerItemDecorator
import io.chthonic.mythos.mvp.FragmentWrapper
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.ArrayList


class MovieVu(inflater: LayoutInflater,
              activity: Activity,
              fragmentWrapper: FragmentWrapper?,
              parentView: ViewGroup?) : ToolbarVu(inflater,
        activity = activity,
        fragmentWrapper = fragmentWrapper,
        parentView = parentView) {

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

    val moviePresentedPublisher: PublishSubject<Movie>? by lazy {
        if (activity is MovieActivity) {
            activity.moviePresentedPublisher
        } else {
            null
        }
    }

    val watchListCheckPublisher: PublishSubject<Boolean> ? by lazy {
        if (activity is MovieActivity) {
            activity.watchListCheckPublisher
        } else {
            null
        }
    }

    val favoriteListCheckPublisher: PublishSubject<Boolean> ? by lazy {
        if (activity is MovieActivity) {
            activity.favoriteListCheckPublisher
        } else {
            null
        }
    }

    private val adapter: MovieAdapter by lazy {
        MovieAdapter(onProfileClickedPicturePublisher)
    }

    override fun onCreate() {
        super.onCreate()

        setUpListView()
    }

    private fun setUpListView(){
        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(activity)

        val itemDecorationDrawable = ResourcesCompat.getDrawable(activity.resources, R.drawable.item_decoration, activity.theme)
        val dividerItemDecoration =  DividerItemDecorator(itemDecorationDrawable)

        listView.addItemDecoration(dividerItemDecoration)
    }

    fun showMovie(movieSummary: MovieSummary) {
        toolbar?.title = movieSummary.screenName

        adapter.movieSummary = movieSummary
        adapter.notifyDataSetChanged()

        val overViewFragment = OverviewFragment.newInstance(movieSummary)
        replaceFragment(overViewFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        (activity as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    fun goToGallery(posters: List<Poster>?) {
        val galleryFragment = MovieGalleryFragment.newInstance()
        val args = Bundle()
        args.putParcelableArrayList(MovieGalleryPresenter.POSTERS, posters as ArrayList<out Parcelable>)
        galleryFragment.arguments =  args
        addFragment(galleryFragment, activity)
    }

    private fun addFragment(fragment: Fragment?, activity: Activity) {
        val fm = (activity as AppCompatActivity).supportFragmentManager
        if (fragment != null && fm != null) {
            fm.beginTransaction().add(android.R.id.content, fragment, null).addToBackStack(null).commit()
        }
    }

    fun updateErrorView(errorMsg: String?) {
        adapter.errorMessage = errorMsg
        adapter.notifyDataSetChanged()
        listView.visibility = View.VISIBLE
    }
}