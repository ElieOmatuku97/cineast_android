package elieomatuku.cineast_android.vu

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.*
import android.view.LayoutInflater
import android.view.ViewGroup
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.adapter.MovieItemAdapter
import kotlinx.android.synthetic.main.vu_movie.view.*
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.os.Parcelable
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
        } }

    fun updateVu(movieSummary: MovieSummary) {
        toolbar?.title = movieSummary.screenName
        updateListView(movieSummary)
        initializeFragmentOnMovieClicked(movieSummary)
    }

    private fun updateListView(movieSummary: MovieSummary) {
        val movieItemAdapter = MovieItemAdapter(movieSummary, onProfileClickedPicturePublisher)
        val itemDecorationDrawable = ResourcesCompat.getDrawable(activity.resources, R.drawable.item_decoration, activity.theme)
        val dividerItemDecorator =  DividerItemDecorator(itemDecorationDrawable)

        configureListView(movieItemAdapter, listView, dividerItemDecorator)
    }

    private fun initializeFragmentOnMovieClicked(movieSummary: MovieSummary) {
        val initialFragmentOnMovieClicked = OverviewFragment.newInstance(movieSummary)
        replaceFragment(initialFragmentOnMovieClicked)
    }

    private fun configureListView(movieItemAdapter: MovieItemAdapter, listView: RecyclerView, dividerItemDecoration: RecyclerView.ItemDecoration?){
        listView.adapter = movieItemAdapter
        listView.layoutManager = LinearLayoutManager(activity)
        if (dividerItemDecoration != null)
            listView.addItemDecoration(dividerItemDecoration)
        movieItemAdapter.notifyDataSetChanged()
    }

    private fun replaceFragment(fragment: Fragment) {
        (activity as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    fun goToGallery(posters: List<Poster>?) {
        val galleryFragment = MovieGalleryFragment.newInstance()
        galleryFragment.arguments = getArgs(posters)
        addFragment(galleryFragment, activity)
    }

    private fun getArgs(posters: List<Poster>?): Bundle {
        val args = Bundle()
        args.putParcelableArrayList(MovieGalleryPresenter.POSTERS, posters as ArrayList<out Parcelable>)
        return args
    }

    private fun addFragment(fragment: Fragment?, activity: Activity) {
        val fm = (activity as AppCompatActivity).supportFragmentManager
        if (fragment != null && fm != null) {
            fm.beginTransaction().add(android.R.id.content, fragment, null).addToBackStack(null).commit()
        }
    }
}